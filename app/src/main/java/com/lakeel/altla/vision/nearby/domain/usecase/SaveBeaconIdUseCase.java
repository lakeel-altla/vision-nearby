package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveBeaconIdUseCase {

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    SaveBeaconIdUseCase() {
    }

    public Single<String> execute() {
        return mPreferenceRepository.saveBeaconId();
    }
}
