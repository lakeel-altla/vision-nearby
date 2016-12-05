package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLineLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLineLinkUseCase {

    @Inject
    FirebaseLineLinksRepository repository;

    @Inject
    FindLineLinkUseCase() {
    }

    public Single<LineLinkEntity> execute(String userId) {
        return repository.findByUserId(userId);
    }
}
