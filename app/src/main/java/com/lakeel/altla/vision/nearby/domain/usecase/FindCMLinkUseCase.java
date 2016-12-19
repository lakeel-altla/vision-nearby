package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindCMLinkUseCase {

    @Inject
    FirebaseCMLinksRepository repository;

    @Inject
    FindCMLinkUseCase() {
    }

    public Single<CmLinkEntity> execute(String userId) {
        return repository.findCmLinksByUserId(userId);
    }
}
