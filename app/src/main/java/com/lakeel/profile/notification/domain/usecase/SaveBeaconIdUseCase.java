package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.PreferenceRepository;

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
