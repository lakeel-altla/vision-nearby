package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserPassingModel;

import java.util.List;

public final class UserPassingModelMapper {

    public UserPassingModel map(HistoryEntity entity) {
        UserPassingModel model = new UserPassingModel();

        model.userId = entity.userId;
        model.userActivity = entity.userActivity;
        model.passingTime = entity.passingTime;

        HistoryEntity.LocationEntity locationEntity = entity.location;
        if (locationEntity != null) {
            model.latitude = locationEntity.latitude;
            model.longitude = locationEntity.longitude;
        }

        HistoryEntity.WeatherEntity weatherEntity = entity.weather;
        if (weatherEntity != null) {
            List<Integer> conditionList = weatherEntity.conditions;
            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }
            model.conditions = conditionArray;
            model.humidity = weatherEntity.humidity;
            model.temperature = weatherEntity.temperature;
        }

        return model;
    }
}
