package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveHistoryUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveHistoryUseCase() {
    }

    public Single<String> execute(String passingUserId, RegionState regionState) {
        String myUserId = MyUser.getUserId();
        return repository.saveHistory(myUserId, passingUserId, regionState).subscribeOn(Schedulers.io());
    }
}
