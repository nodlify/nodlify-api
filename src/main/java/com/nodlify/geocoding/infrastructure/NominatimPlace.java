package com.nodlify.geocoding.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
record NominatimPlace(
        @JsonProperty("place_id") Long placeId,
        String lat,
        String lon,
        String name,
        @JsonProperty("display_name") String displayName,
        String error,
        Address address
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Address(
            String road,
            @JsonProperty("house_number") String houseNumber,
            String city,
            String town,
            String village,
            String municipality,
            String hamlet,
            String postcode
    ) {

        String locality() {
            return firstNonBlank(city, town, village, municipality, hamlet);
        }

        private static String firstNonBlank(String... values) {
            for (var value : values) {
                if (value != null && !value.isBlank()) {
                    return value;
                }
            }
            return null;
        }
    }
}
