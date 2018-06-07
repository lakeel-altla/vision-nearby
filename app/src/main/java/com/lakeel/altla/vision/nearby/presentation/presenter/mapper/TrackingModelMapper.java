package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.domain.model.Location;
import com.lakeel.altla.vision.nearby.domain.model.LocationMeta;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.TrackingModel;

public final class TrackingModelMapper {

    private TrackingModelMapper() {
    }

    public static TrackingModel map(@NonNull Location location) {
        TrackingModel model = new TrackingModel();

        LocationMeta metaData = location.metaData;
        if (metaData != null) {
            model.foundTime = (Long) metaData.passingTime;
        }

        GeoLocation geoLocation = location.geoLocation;
        if (geoLocation != null) {
            model.geoLocation = geoLocation;
        }

        return model;
    }
}
