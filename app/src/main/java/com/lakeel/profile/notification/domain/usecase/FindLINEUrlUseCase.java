package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.LINELinksEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLINEUrlUseCase {

    @Inject
    FirebaseLINELinksRepository mFirebaseLINELinksRepository;

    @Inject
    FindLINEUrlUseCase() {
    }

    public Single<LINELinksEntity> execute(String userId) {
        return mFirebaseLINELinksRepository.findByUserId(userId);
    }
}
