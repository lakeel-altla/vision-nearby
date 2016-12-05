package com.lakeel.altla.vision.nearby.presentation.intent;

import java.io.Serializable;

public final class HistoryBundleData implements Serializable {

    public String userId;

    public String userName;

    public String latitude;

    public String longitude;

    public int detectedActivity;

    public Weather weather;

    public long timestamp;

    public static class Weather implements Serializable {

        public int[] conditions;

        public int humidity;

        public float temperature;
    }
}
