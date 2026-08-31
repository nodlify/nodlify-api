package com.nodlify.geocoding.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Public - Geocoding", description = "Address search and reverse geocoding proxy")
class GeocodingApi {

    private final NominatimClient client;

    @GetMapping("/search")
    @Operation(summary = "Search places", description = "Forward geocoding: free-text query to candidate places")
    ResponseEntity<List<GeoResult>> search(@RequestParam("q") String query) {
        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(client.search(query.strip()));
    }

    @GetMapping("/reverse")
    @Operation(summary = "Reverse geocode", description = "Resolves a latitude/longitude pair to a single address")
    ResponseEntity<GeoResult> reverse(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return client.reverse(lat, lon)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
