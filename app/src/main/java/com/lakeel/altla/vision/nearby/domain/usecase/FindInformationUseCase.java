package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseInformationRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindInformationUseCase {

    @Inject
    FirebaseInformationRepository repository;

    @Inject
    FindInformationUseCase() {
    }

    public Single<InformationEntity> execute(String informationId) {
        String userId = MyUser.getUserId();
        return repository.find(userId, informationId).subscribeOn(Schedulers.io());
    }
}
