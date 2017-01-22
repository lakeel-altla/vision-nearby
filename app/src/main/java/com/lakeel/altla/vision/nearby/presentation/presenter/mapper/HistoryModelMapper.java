package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity.LocationEntity;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryUserEntity;
import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import java.util.List;

public final class HistoryModelMapper {

    public HistoryModel map(HistoryUserEntity historyUserEntity) {
        HistoryEntity historyEntity = historyUserEntity.historyEntity;
        UserEntity userEntity = historyUserEntity.userEntity;

        HistoryModel model = new HistoryModel();
        model.historyId = historyEntity.uniqueId;
        model.userId = historyEntity.userId;
        model.userActivity = historyEntity.userActivity;
        model.passingTime = historyEntity.passingTime;
        model.userName = userEntity.name;
        model.imageUri = userEntity.imageUri;

        LocationEntity locationEntity = historyEntity.location;
        if (locationEntity != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = locationEntity.latitude;
            locationModel.longitude = locationEntity.longitude;
            model.locationModel = locationModel;
        }

        HistoryEntity.WeatherEntity weatherEntity = historyEntity.weather;
        if (weatherEntity != null) {
            List<Integer> conditionList = weatherEntity.conditions;
            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }
            WeatherModel weatherModel = new WeatherModel();
            weatherModel.conditions = conditionArray;
            weatherModel.humidity = weatherEntity.humidity;
            weatherModel.temperature = weatherEntity.temperature;
            model.weather = weatherModel;
        }

        return model;
    }
}
