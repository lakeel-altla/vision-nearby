package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserInformationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Information;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindInformationUseCase {

    @Inject
    FirebaseUserInformationRepository repository;

    @Inject
    FindInformationUseCase() {
    }

    public Single<Information> execute(String informationId) {
        String userId = MyUser.getUserId();
        return repository.findInformation(userId, informationId).subscribeOn(Schedulers.io());
    }
}
