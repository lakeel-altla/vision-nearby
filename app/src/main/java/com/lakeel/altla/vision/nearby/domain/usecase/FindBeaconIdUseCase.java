package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.BeaconIdEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindBeaconIdUseCase {

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    FindBeaconIdUseCase() {
    }

    public Single<BeaconIdEntity> execute() {
        return mPreferenceRepository.findBeaconId();
    }
}
