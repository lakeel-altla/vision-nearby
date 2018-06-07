package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserLocationUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    SaveUserLocationUseCase() {
    }

    public Completable execute(@NonNull String historyId, @NonNull Location location) {
        String userId = CurrentUser.getUid();
        return repository.saveLocation(userId, historyId, location).subscribeOn(Schedulers.io());
    }
}
