package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.domain.model.Preference;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Single;

public final class FindPreferenceUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindPreferenceUseCase() {
    }

    public Single<Preference> execute() {
        String userId = CurrentUser.getUid();
        return repository.findPreferences(userId);
    }

}