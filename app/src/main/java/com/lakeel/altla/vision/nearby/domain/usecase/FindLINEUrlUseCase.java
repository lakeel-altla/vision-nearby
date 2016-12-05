package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLineLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLineUrlUseCase {

    @Inject
    FirebaseLineLinksRepository mFirebaseLineLinksRepository;

    @Inject
    FindLineUrlUseCase() {
    }

    public Single<LineLinkEntity> execute(String userId) {
        return mFirebaseLineLinksRepository.findByUserId(userId);
    }
}
