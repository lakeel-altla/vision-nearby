package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;
import com.lakeel.profile.notification.data.entity.ItemsEntity;

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
