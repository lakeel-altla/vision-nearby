package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.google.android.gms.awareness.state.Weather;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveWeatherUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    SaveWeatherUseCase() {
    }

    public Completable execute(@NonNull String historyId, @NonNull Weather weather) {
        String userId = CurrentUser.getUid();
        return repository.saveWeather(userId, historyId, weather).subscribeOn(Schedulers.io());
    }
}