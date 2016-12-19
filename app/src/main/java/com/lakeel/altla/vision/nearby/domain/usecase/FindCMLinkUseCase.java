package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCmLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindCmLinkUseCase {

    @Inject
    FirebaseCmLinksRepository repository;

    @Inject
    FindCmLinkUseCase() {
    }

    public Single<CmLinkEntity> execute(String userId) {
        return repository.findCmLinksByUserId(userId);
    }
}
