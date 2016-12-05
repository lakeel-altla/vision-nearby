package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.awareness.state.Weather;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveWeatherUseCase {

    @Inject
    FirebaseRecentlyRepository repository;

    @Inject
    SaveWeatherUseCase() {
    }

    public Single<RecentlyEntity> execute(String uniqueId, String userId, Weather weather) {
        return repository.saveWeather(uniqueId, userId, weather);
    }
}
