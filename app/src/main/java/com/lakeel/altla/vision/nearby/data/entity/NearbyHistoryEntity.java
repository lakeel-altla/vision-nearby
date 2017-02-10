package com.lakeel.altla.vision.nearby.data.entity;

import java.util.List;

public final class NearbyHistoryEntity {

    public String userId;

    public boolean isEntered;

    public Integer userActivity;

    public LocationEntity location;

    public WeatherEntity weather;

    public Object passingTime;

    public static class LocationEntity {

        public String latitude;

        public String longitude;
    }

    public static class WeatherEntity {

        public List<Integer> conditions;

        public int humidity;

        public float temperature;
    }
}