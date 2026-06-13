package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;


@Entity
@Table(name = "geo_locations")
@EntityListeners(AuditingEntityListener.class)
public class GeoLocation {

    @EmbeddedId
    private final Identifier id;

    @Embedded
    private LocationDetails locationDetails;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    protected GeoLocation() {
        this.id = Identifier.generate();
    }

    private GeoLocation(LocationDetails locationDetails) {
        this();
        this.locationDetails = locationDetails;
    }

    public static GeoLocation of(LocationDetails locationDetails) {
        return new GeoLocation(locationDetails);
    }

    public LocationDetails locationData() {
        return locationDetails;
    }
}
