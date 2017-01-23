package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseInformationRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindInformationListUseCase {

    @Inject
    FirebaseInformationRepository repository;

    @Inject
    FindInformationListUseCase() {
    }

    public Observable<InformationEntity> execute() {
        String userId = MyUser.getUserId();
        return repository.findList(userId).subscribeOn(Schedulers.io());
    }
}
