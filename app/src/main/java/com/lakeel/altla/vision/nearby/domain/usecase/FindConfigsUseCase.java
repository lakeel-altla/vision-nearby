package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.ConfigsEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConfigsRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindConfigsUseCase {

    @Inject
    FirebaseConfigsRepository mFirebaseConfigsRepository;

    @Inject
    FindConfigsUseCase() {
    }

    public Single<ConfigsEntity> execute() {
        return mFirebaseConfigsRepository.find();
    }
}
