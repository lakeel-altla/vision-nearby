package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;
import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;

import javax.inject.Inject;

import rx.Single;

public final class FindItemUseCase {

    @Inject
    FirebaseItemsRepository mFirebaseItemsRepository;

    @Inject
    public FindItemUseCase() {
    }

    public Single<ItemsEntity> execute(String id) {
        return mFirebaseItemsRepository.findItemsById(id);
    }
}
