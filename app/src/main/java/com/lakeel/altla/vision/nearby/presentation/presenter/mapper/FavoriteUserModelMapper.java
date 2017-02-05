package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;

public final class FavoriteUserModelMapper {

    public FavoriteUserModel map(NearbyHistory nearbyHistory) {
        FavoriteUserModel model = new FavoriteUserModel();

        model.userId = nearbyHistory.userId;
        model.userActivity = nearbyHistory.userActivity;
        model.passingTime = nearbyHistory.passingTime;

        NearbyHistory.Location location = nearbyHistory.location;
        if (location != null) {
            model.latitude = location.latitude;
            model.longitude = location.longitude;
        }

        NearbyHistory.Weather weather = nearbyHistory.weather;
        if (weather != null) {
            model.conditions = weather.conditions;
            model.humidity = weather.humidity;
            model.temperature = weather.temperature;
        }

        return model;
    }
}
