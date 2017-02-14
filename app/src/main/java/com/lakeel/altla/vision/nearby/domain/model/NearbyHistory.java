package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

import java.util.List;

public final class NearbyHistory {

    @Exclude
    public String historyId;

    public String userId;

    public boolean isEntered;

    public Integer userActivity;

    public Location location;

    public Weather weather;

    public Object passingTime;

    public static class Location {

        public String latitude;

        public String longitude;
    }

    public static class Weather {

        public List<Integer> conditions;

        public int humidity;

        public float temperature;
    }
}