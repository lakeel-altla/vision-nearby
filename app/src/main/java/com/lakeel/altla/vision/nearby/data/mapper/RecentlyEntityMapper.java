package com.lakeel.altla.vision.nearby.data.mapper;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;

import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import static com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity.LocationEntity;
import static com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity.WeatherEntity;

public final class RecentlyEntityMapper {

    public RecentlyEntity map(String userId) {
        RecentlyEntity entity = new RecentlyEntity();
        entity.userId = userId;
        return entity;
    }

    public RecentlyEntity map(DetectedActivity detectedActivity) {
        RecentlyEntity entity = new RecentlyEntity();
        if (detectedActivity != null) {
            entity.userActivity = detectedActivity.getType();
        }
        return entity;
    }

    public RecentlyEntity map(Location location) {
        RecentlyEntity entity = new RecentlyEntity();
        if (location != null) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.latitude = String.valueOf(location.getLatitude());
            locationEntity.longitude = String.valueOf(location.getLongitude());
            entity.location = locationEntity;
        }
        return entity;
    }

    public RecentlyEntity map(Weather weather) {
        RecentlyEntity entity = new RecentlyEntity();
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
