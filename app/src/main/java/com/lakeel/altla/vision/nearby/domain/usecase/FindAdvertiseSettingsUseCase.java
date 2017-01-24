package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindAdvertiseSettingsUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindAdvertiseSettingsUseCase() {
    }

    public Single<Boolean> execute() {
        return repository.findAdvertiseSettings().subscribeOn(Schedulers.io());
    }
}
