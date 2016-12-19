package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.ConfigEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConfigsRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindConfigsUseCase {

    @Inject
    FirebaseConfigsRepository repository;

    @Inject
    FindConfigsUseCase() {
    }

    public Single<ConfigEntity> execute() {
        return repository.find();
    }
}
