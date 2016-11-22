package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import android.os.Build;

import javax.inject.Inject;

import rx.Single;

public final class SaveUserBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository mRepository;

    @Inject
    SaveUserBeaconUseCase() {
    }

    public Single<String> execute(String beaconId) {
        return mRepository.saveUserBeacon(beaconId, Build.MODEL);
    }
}
