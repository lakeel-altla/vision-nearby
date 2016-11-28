package com.lakeel.altla.vision.nearby.presentation.presenter.model;

public final class RecentlyItemModel {

    public String userId;

    public String imageUri;

    public String name;

    public LocationModel locationModel;

    public Weather weather;

    public Integer detectedActivity;

    public long passingTime;

    public static class Weather {

        public int[] conditions;

        public int humidity;

        public float temperature;
    }
}
