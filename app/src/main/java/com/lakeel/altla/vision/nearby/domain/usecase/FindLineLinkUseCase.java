package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.LINELinksRepository;
import com.lakeel.altla.vision.nearby.domain.model.LineLink;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindLineLinkUseCase {

    @Inject
    LINELinksRepository repository;

    @Inject
    FindLineLinkUseCase() {
    }

    public Single<LineLink> execute(String userId) {
        return repository.find(userId).subscribeOn(Schedulers.io());
    }
}