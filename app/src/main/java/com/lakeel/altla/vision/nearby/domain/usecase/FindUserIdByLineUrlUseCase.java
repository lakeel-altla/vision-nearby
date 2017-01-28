package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindUserIdByLineUrlUseCase {

    @Inject
    FirebaseLINELinksRepository repository;

    @Inject
    FindUserIdByLineUrlUseCase() {
    }

    public Single<LineLinkEntity> execute(String lineUrl) {
        return repository.findUserIdByLineUrl(lineUrl);
    }

}
