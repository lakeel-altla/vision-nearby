package com.lakeel.altla.vision.nearby.domain.usecase;

import android.os.Build;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveBeaconUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    BeaconRepository beaconsRepository;

    @Inject
    UserProfileRepository usersRepository;

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
        return usersRepository.saveUserBeacon(userId, beaconId).subscribeOn(Schedulers.io());
    }

    private Single<String> saveBeacon(String beaconId, String userId) {
        return beaconsRepository.save(beaconId, userId, Build.MODEL).subscribeOn(Schedulers.io());
    }
}
