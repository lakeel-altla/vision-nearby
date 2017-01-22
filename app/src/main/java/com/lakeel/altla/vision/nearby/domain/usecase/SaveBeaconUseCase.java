package com.lakeel.altla.vision.nearby.domain.usecase;

import android.os.Build;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveBeaconUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    FirebaseBeaconsRepository beaconsRepository;

    @Inject
    FirebaseUsersRepository usersRepository;

    @Inject
    SaveBeaconUseCase() {
    }

    public Single<String> execute() {
        String userId = MyUser.getUserId();
        return preferenceRepository.findBeaconId(userId)
                .flatMap(this::saveBeaconId)
                .flatMap(beaconId -> saveUserBeacon(userId, beaconId))
                .flatMap(beaconId -> saveBeacon(beaconId, userId))
                .subscribeOn(Schedulers.io());
    }

    private Single<String> saveBeaconId(String beaconId) {
        if (StringUtils.isEmpty(beaconId)) {
            return preferenceRepository.saveBeaconId(beaconId);
        } else {
            return Single.just(beaconId);
        }
    }

    private Single<String> saveUserBeacon(String userId, String beaconId) {
        return usersRepository.saveBeacon(userId, beaconId).subscribeOn(Schedulers.io());
    }

    private Single<String> saveBeacon(String beaconId, String userId) {
        return beaconsRepository.saveBeacon(beaconId, userId, Build.MODEL).subscribeOn(Schedulers.io());
    }
}
