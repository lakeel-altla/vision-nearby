package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserLocationUseCase {

    @Inject
    FirebaseUserNearbyHistoryRepository repository;

    @Inject
    SaveUserLocationUseCase() {
    }

    public Completable execute(String uniqueId, Location location) {
        String userId = MyUser.getUserId();
        return repository.saveLocation(uniqueId, userId, location).subscribeOn(Schedulers.io());
    }
}
