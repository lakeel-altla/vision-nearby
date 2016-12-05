package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity.LocationEntity;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel.LocationTextModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import java.util.List;
import java.util.Map;

public final class RecentlyModelMapper {

    public RecentlyModel map(@NonNull RecentlyEntity recentlyEntity, @NonNull UserEntity userEntity) {
        RecentlyModel model = new RecentlyModel();

        model.userId = recentlyEntity.userId;
        model.name = userEntity.name;
        model.imageUri = userEntity.imageUri;
        model.detectedActivity = recentlyEntity.userActivity;
        model.passingTime = recentlyEntity.passingTime;

        LocationEntity locationEntity = recentlyEntity.location;
        if (locationEntity != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = locationEntity.latitude;
            locationModel.longitude = locationEntity.longitude;

            Map<String, String> textMap = locationEntity.text;
            if (textMap != null && !textMap.isEmpty()) {
                LocationTextModel locationTextModel = new LocationTextModel();
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
