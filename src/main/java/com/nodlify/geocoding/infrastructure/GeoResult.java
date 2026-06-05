package com.nodlify.geocoding.infrastructure;


record GeoResult(
        String placeId,
        double latitude,
        double longitude,
        String address
) {
}
