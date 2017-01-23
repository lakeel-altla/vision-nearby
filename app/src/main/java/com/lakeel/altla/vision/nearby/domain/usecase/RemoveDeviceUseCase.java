package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class RemoveDeviceUseCase {

    @Inject
    FirebaseBeaconsRepository beaconsRepository;

    @Inject
    FirebaseUsersRepository usersRepository;

    @Inject
    RemoveDeviceUseCase() {
    }

    public Single<String> execute(String beaconId) {
        String userId = MyUser.getUserId();
        return beaconsRepository.removeBeacon(beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(beaconId1 -> removeUserBeacon(userId, beaconId));
    }

    private Single<String> removeUserBeacon(String userId, String beaconId) {
        return usersRepository.removeBeacon(userId, beaconId).subscribeOn(Schedulers.io());
    }
}
