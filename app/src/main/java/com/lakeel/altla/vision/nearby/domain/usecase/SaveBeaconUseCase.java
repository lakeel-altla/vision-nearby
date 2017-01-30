package com.lakeel.altla.vision.nearby.domain.usecase;

import android.os.Build;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveBeaconUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    FirebaseBeaconRepository beaconsRepository;

    @Inject
    FirebaseUserProfileRepository usersRepository;

    @Inject
    SaveBeaconUseCase() {
    }

    public Single<String> execute(String beaconId) {
        String userId = MyUser.getUserId();
        return preferenceRepository.saveBeaconId(userId, beaconId)
                .flatMap(id -> saveUserBeacon(userId, beaconId))
                .flatMap(id -> saveBeacon(beaconId, userId))
                .subscribeOn(Schedulers.io());
    }

    private Single<String> saveUserBeacon(String userId, String beaconId) {
        return usersRepository.saveBeacon(userId, beaconId).subscribeOn(Schedulers.io());
    }

    private Single<String> saveBeacon(String beaconId, String userId) {
        return beaconsRepository.saveBeacon(beaconId, userId, Build.MODEL).subscribeOn(Schedulers.io());
    }
}
