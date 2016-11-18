package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;
import com.lakeel.profile.notification.data.entity.ItemsEntity;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveItemUseCase {

    @Inject
    FirebaseItemsRepository mFirebaseItemsRepository;


    @Inject
    public SaveItemUseCase() {
    }

    public Completable execute() {
        return mFirebaseItemsRepository.saveItem();
    }
}
