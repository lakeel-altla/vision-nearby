package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindItemNameUseCase {

    @Inject
    FirebaseItemsRepository mFirebaseItemsRepository;

    @Inject
    FindItemNameUseCase() {
    }

    public Single<ItemsEntity> execute(String name) {
        return mFirebaseItemsRepository.findItemsByName(name);
    }
}
