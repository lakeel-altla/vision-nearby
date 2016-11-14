package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.PreferencesEntity;
import com.lakeel.profile.notification.domain.repository.PreferenceRepository;

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
