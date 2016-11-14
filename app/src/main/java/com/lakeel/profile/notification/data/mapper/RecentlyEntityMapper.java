package com.lakeel.profile.notification.data.mapper;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.firebase.database.ServerValue;

import com.lakeel.profile.notification.data.entity.RecentlyEntity;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import static com.lakeel.profile.notification.data.entity.RecentlyEntity.LocationEntity;
import static com.lakeel.profile.notification.data.entity.RecentlyEntity.WeatherEntity;

public final class RecentlyEntityMapper {

    public RecentlyEntity map(String id, DetectedActivity detectedActivity, Location location, Weather weather) {
        RecentlyEntity entity = new RecentlyEntity();
        entity.id = id;
        if (detectedActivity != null) {
            entity.userActivity = detectedActivity.getType();
        }
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

        if (location != null) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.latitude = String.valueOf(location.getLatitude());
            locationEntity.longitude = String.valueOf(location.getLongitude());
            entity.location = locationEntity;
        }
        return entity;
    }
}
