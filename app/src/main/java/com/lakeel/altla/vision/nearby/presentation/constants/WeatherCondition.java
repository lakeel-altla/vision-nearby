package com.lakeel.altla.vision.nearby.presentation.constants;

import com.google.android.gms.awareness.state.Weather;

public enum WeatherCondition {
    CLEAR(Weather.CONDITION_CLEAR, "Clear"),
    CLOUDY(Weather.CONDITION_CLOUDY, "Cloudy"),
    FOGGY(Weather.CONDITION_FOGGY, "Foggy"),
    HAZY(Weather.CONDITION_HAZY, "Hazy"),
    ICY(Weather.CONDITION_ICY, "Icy"),
    RAINY(Weather.CONDITION_RAINY, "Rainy"),
    SNOWY(Weather.CONDITION_SNOWY, "Snow"),
    STORMY(Weather.CONDITION_STORMY, "Stormy"),
    UNKNOWN(Weather.CONDITION_UNKNOWN, "Unknown"),
    WINDY(Weather.CONDITION_WINDY, "Windy");

    private int intValue;

    private String stringValue;

    WeatherCondition(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public String getWeather() {
        return stringValue;
    }

    public static WeatherCondition toType(int intValue) {
        for (WeatherCondition type : WeatherCondition.values()) {
            if (type.intValue == intValue) {
                return type;
            }
        }
        return WeatherCondition.UNKNOWN;
    }
}
