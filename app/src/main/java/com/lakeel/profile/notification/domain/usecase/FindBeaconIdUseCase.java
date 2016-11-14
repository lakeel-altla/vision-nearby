package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.BeaconIdEntity;
import com.lakeel.profile.notification.domain.repository.PreferenceRepository;

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
