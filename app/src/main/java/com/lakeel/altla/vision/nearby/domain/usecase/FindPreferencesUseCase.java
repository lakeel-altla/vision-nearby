package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindPreferencesUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindPreferencesUseCase() {
    }

    public Single<PreferenceEntity> execute(String userId) {
        return repository.findPreferences(userId);
    }

}
