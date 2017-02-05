package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserInformationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Information;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllInformationUseCase {

    @Inject
    UserInformationRepository repository;

    @Inject
    FindAllInformationUseCase() {
    }

    public Observable<Information> execute() {
        String userId = MyUser.getUserId();
        return repository.findAll(userId).subscribeOn(Schedulers.io());
    }
}
