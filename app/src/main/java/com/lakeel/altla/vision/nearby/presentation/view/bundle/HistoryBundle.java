package com.lakeel.altla.vision.nearby.presentation.view.bundle;

import java.io.Serializable;

public final class HistoryBundle implements Serializable {

    public String userId;

    public String userName;

    public String latitude;

    public String longitude;

    public int detectedActivity;

    public WeatherBundle weatherBundle;

    public long timestamp;
}
