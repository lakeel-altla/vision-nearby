package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LINELinksEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindUserIdUseCase {

    @Inject
    FirebaseLINELinksRepository repository;

    @Inject
    FindUserIdUseCase() {
    }

    public Single<LINELinksEntity> execute(String lineUrl) {
        return repository.findUserIdByLINEUrl(lineUrl);
    }

}
