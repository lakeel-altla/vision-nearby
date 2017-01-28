package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.History;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserPassingModel;

public final class UserPassingModelMapper {

    public UserPassingModel map(History history) {
        UserPassingModel model = new UserPassingModel();

        model.userId = history.userId;
        model.userActivity = history.userActivity;
        model.passingTime = history.passingTime;

        History.Location location = history.location;
        if (location != null) {
            model.latitude = location.latitude;
            model.longitude = location.longitude;
        }

        History.Weather weather = history.weather;
        if (weather != null) {
            model.conditions = weather.conditions;
            model.humidity = weather.humidity;
            model.temperature = weather.temperature;
        }

        return model;
    }
}
