package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import java.util.List;

public final class FavoriteUserModelMapper {

    private FavoriteUserModelMapper() {
    }

    public static FavoriteUserModel map(@NonNull NearbyHistory nearbyHistory) {
        FavoriteUserModel model = new FavoriteUserModel();

        model.userActivity = nearbyHistory.userActivity;
        model.passingTime = (Long) nearbyHistory.passingTime;

        NearbyHistory.Location location = nearbyHistory.location;
        if (location != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = location.latitude;
            locationModel.longitude = location.longitude;

            model.locationModel = locationModel;
        }

        NearbyHistory.Weather weather = nearbyHistory.weather;
        if (weather != null) {
            List<Integer> conditionList = weather.conditions;

            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }

            WeatherModel weatherModel = new WeatherModel();
            weatherModel.conditions = conditionArray;
            weatherModel.humidity = weather.humidity;
            weatherModel.temperature = weather.temperature;

            model.weatherModel = weatherModel;
        }

        return model;
    }
}
