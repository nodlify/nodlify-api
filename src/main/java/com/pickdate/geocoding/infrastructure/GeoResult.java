package com.pickdate.geocoding.infrastructure;


record GeoResult(
        String placeId,
        double latitude,
        double longitude,
        String address
) {
}
