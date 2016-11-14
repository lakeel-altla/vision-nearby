package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.ConfigsEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseConfigsRepository;

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
