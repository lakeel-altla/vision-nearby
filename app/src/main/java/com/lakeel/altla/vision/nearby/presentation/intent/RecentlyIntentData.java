package com.lakeel.altla.vision.nearby.presentation.intent;

import java.io.Serializable;

public final class RecentlyIntentData implements Serializable {

    public String key;

    public String id;

    public String latitude;

    public String longitude;

    public String locationText;

    public int detectedUserActivity;

    public Weather weather;

    public long timestamp;

    public static class Weather implements Serializable {

        public int[] conditions;

        public int humidity;

        public float temperature;
    }
}
