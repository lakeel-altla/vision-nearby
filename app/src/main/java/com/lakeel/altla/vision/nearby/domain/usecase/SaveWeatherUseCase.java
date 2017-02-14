package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.awareness.state.Weather;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveWeatherUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    SaveWeatherUseCase() {
    }

    public Completable execute(String uniqueId, Weather weather) {
        String userId = CurrentUser.getUid();
        return repository.saveWeather(uniqueId, userId, weather).subscribeOn(Schedulers.io());
    }
}