package com.lakeel.altla.vision.nearby.domain.model;

public final class NearbyHistory {

    public String historyId;

    public String userId;

    public Integer userActivity;

    public Location location;

    public Weather weather;

    public long passingTime;

    public static class Location {

        public String latitude;

        public String longitude;
    }

    public static class Weather {

        public int[] conditions;

        public int humidity;

        public float temperature;
    }

}
