package com.pickdate.geocoding.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = "geocoding")
class NominatimClient {

    private final RestClient geocodingRestClient;
    private final GeocodingConfig config;

    @Cacheable(key = "'q:' + #query.toLowerCase()", unless = "#result.isEmpty()")
    public List<GeoResult> search(String query) {
        try {
            var places = geocodingRestClient.get()
                    .uri(uri -> uri.path("/search")
                            .queryParam("q", query)
                            .queryParam("format", "json")
                            .queryParam("addressdetails", 1)
                            .queryParam("limit", config.getSearchLimit())
                            .build())
                    .retrieve()
                    .body(NominatimPlace[].class);

            if (places == null) {
                return List.of();
            }
            return Arrays.stream(places)
                    .filter(NominatimClient::hasCoordinates)
                    .map(NominatimClient::toResult)
                    .toList();
        } catch (RestClientException ex) {
            log.warn("Nominatim search failed for query '{}': {}", query, ex.getMessage());
            return List.of();
        }
    }

    @Cacheable(
            key = "'rev:' + T(java.lang.Math).round(#latitude * 100000) + ':' + T(java.lang.Math).round(#longitude * 100000)",
            unless = "#result == null"
    )
    public Optional<GeoResult> reverse(double latitude, double longitude) {
        try {
            var place = geocodingRestClient.get()
                    .uri(uri -> uri.path("/reverse")
                            .queryParam("lat", latitude)
                            .queryParam("lon", longitude)
                            .queryParam("format", "json")
                            .queryParam("addressdetails", 1)
                            .build())
                    .retrieve()
                    .body(NominatimPlace.class);

            if (place == null || place.error() != null || !hasCoordinates(place)) {
                return Optional.empty();
            }
            return Optional.of(toResult(place));
        } catch (RestClientException ex) {
            log.warn("Nominatim reverse lookup failed for {},{}: {}", latitude, longitude, ex.getMessage());
            return Optional.empty();
        }
    }

    private static boolean hasCoordinates(NominatimPlace place) {
        return place.lat() != null && place.lon() != null;
    }

    private static GeoResult toResult(NominatimPlace place) {
        return new GeoResult(
                place.placeId() == null ? null : String.valueOf(place.placeId()),
                Double.parseDouble(place.lat()),
                Double.parseDouble(place.lon()),
                composeAddress(place)
        );
    }

    private static String composeAddress(NominatimPlace place) {
        var address = place.address();
        if (address == null) {
            return place.displayName();
        }

        var parts = new ArrayList<String>();
        var name = place.name();
        var street = street(address);

        if (isPresent(name) && !name.equalsIgnoreCase(address.road())) {
            parts.add(name.strip());
        }
        if (isPresent(street)) {
            parts.add(street);
        }
        if (isPresent(address.locality())) {
            parts.add(address.locality().strip());
        }
        if (isPresent(address.postcode())) {
            parts.add(address.postcode().strip());
        }

        return parts.isEmpty() ? place.displayName() : String.join(", ", parts);
    }

    private static String street(NominatimPlace.Address address) {
        if (!isPresent(address.road())) {
            return null;
        }
        return isPresent(address.houseNumber())
                ? address.road().strip() + " " + address.houseNumber().strip()
                : address.road().strip();
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }
}
