package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.domain.model.Preference;
import com.lakeel.altla.vision.nearby.domain.model.Presence;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;

public final class FindPreferencesUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindPreferencesUseCase() {
    }

    public Single<Preference> execute() {
        String userId = MyUser.getUserId();
        return repository.findPreferences(userId);
    }

}
