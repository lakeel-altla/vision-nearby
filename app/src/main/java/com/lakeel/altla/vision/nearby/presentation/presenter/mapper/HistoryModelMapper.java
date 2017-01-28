package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.History;
import com.lakeel.altla.vision.nearby.domain.model.HistoryUser;
import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import static com.lakeel.altla.vision.nearby.domain.model.History.Location;
import static com.lakeel.altla.vision.nearby.domain.model.History.Weather;

public final class HistoryModelMapper {

    public HistoryModel map(HistoryUser historyUser) {
        History history = historyUser.history;
        User user = historyUser.user;

        HistoryModel model = new HistoryModel();
        model.historyId = history.historyId;
        model.userId = history.userId;
        model.passingTime = history.passingTime;
        model.userName = user.name;
        model.imageUri = user.imageUri;

        if (history.userActivity != null) {
            model.userActivity = history.userActivity;
        }

        Location location = history.location;
        if (location != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = location.latitude;
            locationModel.longitude = location.longitude;
            model.locationModel = locationModel;
        }

        Weather weather = history.weather;
        if (weather != null) {
            WeatherModel weatherModel = new WeatherModel();
            weatherModel.conditions = weather.conditions;
            weatherModel.humidity = weather.humidity;
            weatherModel.temperature = weather.temperature;
            model.weather = weatherModel;
        }

        return model;
    }
}
