package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindCMLinksUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    FindCMLinksUseCase() {
    }

    public Single<CMLinkEntity> execute(String userId) {
        return mFirebaseCMLinksRepository.findCmLinksByUserId(userId);
    }
}
