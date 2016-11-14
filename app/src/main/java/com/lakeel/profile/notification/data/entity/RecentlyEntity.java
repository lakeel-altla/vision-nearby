package com.lakeel.profile.notification.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RecentlyEntity extends BaseEntity {

    public String id;

    public Integer userActivity;

    public LocationEntity location;

    public WeatherEntity weather;

    public long passingTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userActivity", userActivity);
        map.put("location", location);
        map.put("weather", weather);
        map.put("passingTime", ServerValue.TIMESTAMP);
        return map;
    }

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
}
