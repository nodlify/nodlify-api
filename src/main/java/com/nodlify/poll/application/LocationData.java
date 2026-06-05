package com.nodlify.poll.application;

import com.nodlify.poll.domain.GeoLocation;


public record LocationData(
        double latitude,
        double longitude,
        String placeId,
        String address
) {

    static LocationData from(GeoLocation location) {
        if (location == null || location.locationData() == null) {
            return null;
        }
        var details = location.locationData();
        return new LocationData(details.latitude(), details.longitude(), details.placeId(), details.address());
    }
}
