package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NearbyHistoryEntity {

    private static final String USER_ID = "userId";

    private static final String IS_ENTERED = "isEntered";

    private static final String PASSING_TIME = "passingTime";

    private static final String USER_ACTIVITY = "userActivity";

    private static final String LOCATION = "location";

    private static final String WEATHER = "weather";

    public String userId;

    public boolean isEntered;

    public Integer userActivity;

    public LocationEntity location;

    public WeatherEntity weather;

    public long passingTime;

    public static class LocationEntity {

        public String latitude;

        public String longitude;
    }

    public static class WeatherEntity {

        public List<Integer> conditions;

        public int humidity;

        public float temperature;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, userId);
        map.put(IS_ENTERED, isEntered);
        map.put(PASSING_TIME, ServerValue.TIMESTAMP);
        return map;
    }

    public Map<String, Object> toUserActivityMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ACTIVITY, userActivity);
        return map;
    }

    public Map<String, Object> toLocationMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(LOCATION, location);
        return map;
    }

    public Map<String, Object> toWeatherMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(WEATHER, weather);
        return map;
    }
}