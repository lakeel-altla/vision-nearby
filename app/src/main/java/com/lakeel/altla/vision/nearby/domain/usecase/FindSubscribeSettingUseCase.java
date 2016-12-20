package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindSubscribeSettingUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindSubscribeSettingUseCase() {
    }

    public Single<Boolean> execute() {
        return repository.findSubscribeSetting();
    }
}
