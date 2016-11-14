package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.CMLinksEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindCMLinksUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    FindCMLinksUseCase() {
    }

    public Single<CMLinksEntity> execute() {
        return mFirebaseCMLinksRepository.findCmLinks();
    }
}
