package com.lakeel.altla.vision.nearby.data.mapper.entity;

import android.location.Location;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;

import java.util.ArrayList;
import java.util.List;

import static com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity.LocationEntity;
import static com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity.WeatherEntity;

public final class HistoryEntityMapper {

    public NearbyHistoryEntity map(String userId, RegionState regionState) {
        NearbyHistoryEntity entity = new NearbyHistoryEntity();
        entity.userId = userId;
        entity.isEntered = RegionState.ENTER == regionState;
        entity.passingTime = ServerValue.TIMESTAMP;
        return entity;
    }

    public NearbyHistoryEntity map(DetectedActivity userActivity) {
        NearbyHistoryEntity entity = new NearbyHistoryEntity();
        if (userActivity != null) {
            entity.userActivity = userActivity.getType();
        }
        return entity;
    }

    public NearbyHistoryEntity map(Location location) {
        NearbyHistoryEntity entity = new NearbyHistoryEntity();
        if (location != null) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.latitude = String.valueOf(location.getLatitude());
            locationEntity.longitude = String.valueOf(location.getLongitude());
            entity.location = locationEntity;
        }
        return entity;
    }

    public NearbyHistoryEntity map(Weather weather) {
        NearbyHistoryEntity entity = new NearbyHistoryEntity();
        if (weather != null) {
            entity.weather = new WeatherEntity();
            entity.weather.humidity = weather.getHumidity();
            entity.weather.temperature = weather.getTemperature(Weather.CELSIUS);

            List<Integer> conditions = new ArrayList<>(weather.getConditions().length);
            for (int condition : weather.getConditions()) {
                conditions.add(condition);
            }
            entity.weather.conditions = conditions;
        }
        return entity;
    }
}
