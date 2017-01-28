package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.awareness.state.Weather;
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveWeatherUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveWeatherUseCase() {
    }

    public Completable execute(String uniqueId, Weather weather) {
        String userId = MyUser.getUserId();
        return repository.saveWeather(uniqueId, userId, weather).subscribeOn(Schedulers.io());
    }
}