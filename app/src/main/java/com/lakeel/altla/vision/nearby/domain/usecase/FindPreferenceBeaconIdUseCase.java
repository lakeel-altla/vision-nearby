package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.PreferenceBeaconIdEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindPreferenceBeaconIdUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindPreferenceBeaconIdUseCase() {
    }

    public Single<PreferenceBeaconIdEntity> execute() {
        return repository.findBeaconId();
    }
}