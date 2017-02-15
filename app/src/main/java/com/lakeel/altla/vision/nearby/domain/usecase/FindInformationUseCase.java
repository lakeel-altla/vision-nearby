package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserInformationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Information;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindInformationUseCase {

    @Inject
    UserInformationRepository repository;

    @Inject
    FindInformationUseCase() {
    }

    public Single<Information> execute(String informationId) {
        String userId = CurrentUser.getUid();
        return repository.find(userId, informationId).subscribeOn(Schedulers.io());
    }
}
