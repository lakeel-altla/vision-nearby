package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistoryUserProfile;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyHistoryModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import static com.lakeel.altla.vision.nearby.domain.model.NearbyHistory.Location;
import static com.lakeel.altla.vision.nearby.domain.model.NearbyHistory.Weather;

public final class HistoryModelMapper {

    public NearbyHistoryModel map(NearbyHistoryUserProfile nearbyHistoryUserProfile) {
        NearbyHistory nearbyHistory = nearbyHistoryUserProfile.nearbyHistory;
        UserProfile userProfile = nearbyHistoryUserProfile.userProfile;

        NearbyHistoryModel model = new NearbyHistoryModel();
        model.historyId = nearbyHistory.historyId;
        model.userId = nearbyHistory.userId;
        model.passingTime = nearbyHistory.passingTime;
        model.userName = userProfile.name;
        model.imageUri = userProfile.imageUri;

        if (nearbyHistory.userActivity != null) {
            model.userActivity = nearbyHistory.userActivity;
        }

        Location location = nearbyHistory.location;
        if (location != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = location.latitude;
            locationModel.longitude = location.longitude;
            model.locationModel = locationModel;
        }

        Weather weather = nearbyHistory.weather;
        if (weather != null) {
            WeatherModel weatherModel = new WeatherModel();
            weatherModel.conditions = weather.conditions;
            weatherModel.humidity = weather.humidity;
            weatherModel.temperature = weather.temperature;
            model.weather = weatherModel;
        }

        return model;
    }
}
