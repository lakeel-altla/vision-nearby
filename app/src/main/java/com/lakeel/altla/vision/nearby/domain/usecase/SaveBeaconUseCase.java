package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import android.os.Build;

import javax.inject.Inject;

import rx.Single;

public final class SaveBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository mRepository;

    @Inject
    SaveBeaconUseCase() {
    }

    public Single<String> execute(String beaconId) {
        return mRepository.saveBeacon(beaconId, Build.MODEL);
    }
}
