package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;

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
