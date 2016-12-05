package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class SavePreferenceBeaconIdUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    SavePreferenceBeaconIdUseCase() {
    }

    public Single<String> execute() {
        return repository.saveBeaconId();
    }
}
