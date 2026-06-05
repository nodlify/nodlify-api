package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.LocationDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateLocationRequest {

    private double latitude;
    private double longitude;
    private String placeId;
    private String address;

    LocationDetails toLocationData() {
        return new LocationDetails(latitude, longitude, placeId, address);
    }
}
