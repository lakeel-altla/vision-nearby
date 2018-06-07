package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class RemoveDeviceUseCase {

    @Inject
    BeaconRepository beaconsRepository;

    @Inject
    UserProfileRepository usersRepository;

    @Inject
    RemoveDeviceUseCase() {
    }

    public Single<String> execute(@NonNull String beaconId) {
        String userId = CurrentUser.getUid();

        return beaconsRepository
                .remove(beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(beaconId1 -> removeUserBeacon(userId, beaconId));
    }

    private Single<String> removeUserBeacon(String userId, String beaconId) {
        return usersRepository.removeUserBeacon(userId, beaconId).subscribeOn(Schedulers.io());
    }
}
