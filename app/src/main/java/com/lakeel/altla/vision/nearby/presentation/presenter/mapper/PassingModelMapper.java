package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import java.util.List;
import java.util.Map;

public final class PassingModelMapper {

    public PassingModel map(@NonNull RecentlyEntity recentlyEntity) {
        PassingModel model = new PassingModel();

        model.detectedActivity = recentlyEntity.userActivity;
        model.passingTime = recentlyEntity.passingTime;

        RecentlyEntity.LocationEntity locationEntity = recentlyEntity.location;
        if (locationEntity != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = locationEntity.latitude;
            locationModel.longitude = locationEntity.longitude;

            Map<String, String> textMap = locationEntity.text;
            if (textMap != null && !textMap.isEmpty()) {
                LocationModel.LocationTextModel locationTextModel = new LocationModel.LocationTextModel();
                locationTextModel.mTextMap = textMap;
                locationModel.mLocationTextModel = locationTextModel;
            }

            model.locationModel = locationModel;
        }

        if (recentlyEntity.weather != null) {
            List<Integer> conditionList = recentlyEntity.weather.conditions;
            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }
            WeatherModel weather = new WeatherModel();
            weather.conditions = conditionArray;
            weather.humidity = recentlyEntity.weather.humidity;
            weather.temperature = recentlyEntity.weather.temperature;
            model.weather = weather;
        }

        return model;
    }
}
