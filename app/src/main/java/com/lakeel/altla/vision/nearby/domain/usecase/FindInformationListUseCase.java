package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseInformationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Information;
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

    public Observable<Information> execute() {
        String userId = MyUser.getUserId();
        return repository.findInformationList(userId).subscribeOn(Schedulers.io());
    }
}
