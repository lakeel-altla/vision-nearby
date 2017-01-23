package com.lakeel.altla.vision.nearby.presentation.awareness;

import com.google.android.gms.awareness.state.Weather;
import com.lakeel.altla.vision.nearby.R;

public enum WeatherCondition {
    CLEAR(Weather.CONDITION_CLEAR, R.string.weather_condition_clear),
    CLOUDY(Weather.CONDITION_CLOUDY, R.string.weather_condition_cloudy),
    FOGGY(Weather.CONDITION_FOGGY, R.string.weather_condition_foggy),
    HAZY(Weather.CONDITION_HAZY, R.string.weather_condition_hazy),
    ICY(Weather.CONDITION_ICY, R.string.weather_condition_icy),
    RAINY(Weather.CONDITION_RAINY, R.string.weather_condition_rainy),
    SNOWY(Weather.CONDITION_SNOWY, R.string.weather_condition_snow),
    STORMY(Weather.CONDITION_STORMY, R.string.weather_condition_stormy),
    UNKNOWN(Weather.CONDITION_UNKNOWN, R.string.weather_condition_unknown),
    WINDY(Weather.CONDITION_WINDY, R.string.weather_condition_windy);

    private int conditionValue;

    private int resValue;

    WeatherCondition(int conditionValue, int resValue) {
        this.conditionValue = conditionValue;
        this.resValue = resValue;
    }

    public int getResValue() {
        return resValue;
    }

    public static WeatherCondition toWeatherCondition(int intValue) {
        for (WeatherCondition type : WeatherCondition.values()) {
            if (type.conditionValue == intValue) {
                return type;
            }
        }
        return WeatherCondition.UNKNOWN;
    }
}
