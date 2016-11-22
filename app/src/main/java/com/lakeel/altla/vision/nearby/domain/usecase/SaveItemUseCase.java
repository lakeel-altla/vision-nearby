package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;
import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;

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
