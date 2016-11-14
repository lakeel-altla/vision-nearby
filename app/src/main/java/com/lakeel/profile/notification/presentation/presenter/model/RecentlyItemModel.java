package com.lakeel.profile.notification.presentation.presenter.model;

public final class RecentlyItemModel {

    public String mKey;

    public String mId;

    public String mImageUri;

    public String mName;

    public LocationModel mLocationModel;

    public Weather mWeather;

    public Integer mUserActivity;

    public long mPassingTime;

    public static class Weather {

        public int[] mConditions;

        public int humidity;

        public float temparature;
    }
}
