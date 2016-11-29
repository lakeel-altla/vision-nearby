package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RecentlyEntity extends BaseEntity {

    public String userId;

    public Integer userActivity;

    public LocationEntity location;

    public WeatherEntity weather;

    public long passingTime;

    public static class LocationEntity {

        public String latitude;

        public String longitude;

        public HashMap<String, String> text;
    }

    public static class WeatherEntity {

        public List<Integer> conditions;

        public int humidity;

        public float temperature;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("passingTime", ServerValue.TIMESTAMP);
        return map;
    }

    public Map<String, Object> toUserActivityMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("detectedActivity", userActivity);
        return map;
    }

    public Map<String, Object> toLocationMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        return map;
    }

    public Map<String, Object> toWeatherMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("weather", weather);
        return map;
    }
}
