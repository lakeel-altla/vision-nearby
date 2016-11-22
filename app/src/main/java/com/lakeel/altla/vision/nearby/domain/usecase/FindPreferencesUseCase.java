package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.PreferencesEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindPreferencesUseCase {

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    FindPreferencesUseCase() {
    }

    public Single<PreferencesEntity> execute() {
        return mPreferenceRepository.findPreferences();
    }

}
