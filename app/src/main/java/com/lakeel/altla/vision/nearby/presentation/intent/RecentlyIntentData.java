package com.lakeel.altla.vision.nearby.presentation.intent;

import java.io.Serializable;

public final class RecentlyIntentData implements Serializable {

    public String mKey;

    public String mId;

    public String mLatitude;

    public String mLongitude;

    public String mLocationText;

    public int mUserActivity;

    public Weather mWeather;

    public long mTimestamp;

    public static class Weather implements Serializable {

        public int[] mConditions;

        public int mHumidity;

        public float mTemperature;
    }
}
