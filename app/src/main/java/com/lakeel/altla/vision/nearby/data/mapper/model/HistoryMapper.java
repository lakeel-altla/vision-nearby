package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.model.History;

import java.util.List;

import static com.lakeel.altla.vision.nearby.data.entity.HistoryEntity.LocationEntity;
import static com.lakeel.altla.vision.nearby.data.entity.HistoryEntity.WeatherEntity;
import static com.lakeel.altla.vision.nearby.domain.model.History.Location;
import static com.lakeel.altla.vision.nearby.domain.model.History.Weather;

public final class HistoryMapper {

    public History map(HistoryEntity entity, String key) {
        History history = new History();
        history.historyId = key;
        history.userId = entity.userId;
        history.regionState = entity.regionState;
        history.passingTime = entity.passingTime;

        if (entity.userActivity != null) {
            history.userActivity = entity.userActivity;
        }

        LocationEntity locationEntity = entity.location;
        if (locationEntity != null) {
            Location location = new Location();
            location.latitude = locationEntity.latitude;
            location.longitude = locationEntity.longitude;
        }

        WeatherEntity weatherEntity = entity.weather;
        if (weatherEntity != null) {
            List<Integer> conditionList = weatherEntity.conditions;
            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }
            Weather weather = new Weather();
            weather.conditions = conditionArray;
            weather.humidity = weatherEntity.humidity;
            weather.temperature = weatherEntity.temperature;
            history.weather = weather;
        }

        return history;
    }
}
