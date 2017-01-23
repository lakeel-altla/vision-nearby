package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class SavePreferenceBeaconIdUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    SavePreferenceBeaconIdUseCase() {
    }

    public Single<String> execute(String userId) {
        return repository.saveBeaconId(userId);
    }
}
