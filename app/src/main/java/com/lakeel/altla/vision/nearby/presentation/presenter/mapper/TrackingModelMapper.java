package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.domain.model.Location;
import com.lakeel.altla.vision.nearby.domain.model.LocationMetaData;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.TrackingModel;

public final class TrackingModelMapper {

    public TrackingModel map(Location location) {
        TrackingModel model = new TrackingModel();

        LocationMetaData metaData = location.metaData;
        if (metaData != null) {
            model.foundTime = metaData.passingTime;
        }

        GeoLocation geoLocation = location.geoLocation;
        if (geoLocation != null) {
            model.geoLocation = geoLocation;
        }

        return model;
    }
}
