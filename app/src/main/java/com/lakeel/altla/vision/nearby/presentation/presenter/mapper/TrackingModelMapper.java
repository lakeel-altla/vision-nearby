package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.domain.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.domain.entity.LocationEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.TrackingModel;

public final class TrackingModelMapper {

    public TrackingModel map(LocationEntity entity) {
        TrackingModel model = new TrackingModel();

        LocationDataEntity locationDataEntity = entity.locationDataEntity;
        if (locationDataEntity != null) {
            model.foundTime = locationDataEntity.passingTime;
        }

        GeoLocation geoLocation = entity.geoLocation;
        if (geoLocation != null) {
            model.geoLocation = geoLocation;
        }

        return model;
    }
}
