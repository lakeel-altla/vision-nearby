package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.data.repository.firebase.LINELinksRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindLineLinkUseCase {

    @Inject
    LINELinksRepository repository;

    @Inject
    FindLineLinkUseCase() {
    }

    public Single<LineLinkEntity> execute(String userId) {
        return repository.findByUserId(userId).subscribeOn(Schedulers.io());
    }
}
