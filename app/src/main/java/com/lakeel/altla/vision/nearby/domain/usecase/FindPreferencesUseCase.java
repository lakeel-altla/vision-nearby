package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;

public final class FindPreferencesUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindPreferencesUseCase() {
    }

    public Single<PreferenceEntity> execute() {
        String userId = MyUser.getUserId();
        return repository.findPreferences(userId);
    }

}
