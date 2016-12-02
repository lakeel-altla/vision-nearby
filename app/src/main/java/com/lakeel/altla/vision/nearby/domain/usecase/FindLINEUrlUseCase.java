package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LINELinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLINEUrlUseCase {

    @Inject
    FirebaseLINELinksRepository mFirebaseLINELinksRepository;

    @Inject
    FindLINEUrlUseCase() {
    }

    public Single<LINELinkEntity> execute(String userId) {
        return mFirebaseLINELinksRepository.findByUserId(userId);
    }
}
