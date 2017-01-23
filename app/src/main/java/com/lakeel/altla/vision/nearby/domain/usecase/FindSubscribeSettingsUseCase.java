package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindSubscribeSettingsUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindSubscribeSettingsUseCase() {
    }

    public Single<Boolean> execute() {
        return repository.findSubscribeSetting().subscribeOn(Schedulers.io());
    }
}
