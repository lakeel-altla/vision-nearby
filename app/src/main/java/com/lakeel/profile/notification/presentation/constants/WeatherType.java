package com.lakeel.profile.notification.presentation.constants;

import com.google.android.gms.awareness.state.Weather;

public enum WeatherType {
    CONDITION_CLEAR(Weather.CONDITION_CLEAR, "Clear"),
    CONDITION_CLOUDY(Weather.CONDITION_CLOUDY, "Cludy"),
    CONDITION_FOGGY(Weather.CONDITION_FOGGY, "Foggy"),
    CONDITION_HAZY(Weather.CONDITION_HAZY, "Hazy"),
    CONDITION_ICY(Weather.CONDITION_ICY, "Icy"),
    CONDITION_RAINY(Weather.CONDITION_RAINY, "Rainy"),
    CONDITION_SNOWY(Weather.CONDITION_SNOWY, "Snow"),
    CONDITION_STORMY(Weather.CONDITION_STORMY, "Stormy"),
    CONDITION_UNKNOWN(Weather.CONDITION_UNKNOWN, "Unknown"),
    CONDITION_WINDY(Weather.CONDITION_WINDY, "Windy");

    private int mIntValue;

    private String mStringValue;

    WeatherType(int intValue, String stringValue) {
        mIntValue = intValue;
        mStringValue = stringValue;
    }

    public String getWeather() {
        return mStringValue;
    }

    public static WeatherType toType(int intValue) {
        for (WeatherType type : WeatherType.values()) {
            if (type.mIntValue == intValue) {
                return type;
            }
        }
        return WeatherType.CONDITION_UNKNOWN;
    }
}
