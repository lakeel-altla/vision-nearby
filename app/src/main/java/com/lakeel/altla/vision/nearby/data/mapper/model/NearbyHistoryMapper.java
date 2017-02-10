package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;

import java.util.List;

import static com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity.LocationEntity;
import static com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity.WeatherEntity;
import static com.lakeel.altla.vision.nearby.domain.model.NearbyHistory.Location;
import static com.lakeel.altla.vision.nearby.domain.model.NearbyHistory.Weather;

public final class NearbyHistoryMapper {

    public NearbyHistory map(DataSnapshot snapshot) {
        NearbyHistoryEntity entity = snapshot.getValue(NearbyHistoryEntity.class);

        NearbyHistory nearbyHistory = new NearbyHistory();
        nearbyHistory.historyId = snapshot.getKey();
        nearbyHistory.userId = entity.userId;
        nearbyHistory.passingTime = (Long) entity.passingTime;

        if (entity.userActivity != null) {
            nearbyHistory.userActivity = entity.userActivity;
        }

        LocationEntity locationEntity = entity.location;
        if (locationEntity != null) {
            Location location = new Location();
            location.latitude = locationEntity.latitude;
            location.longitude = locationEntity.longitude;
            nearbyHistory.location = location;
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
            nearbyHistory.weather = weather;
        }

        return nearbyHistory;
    }
}
