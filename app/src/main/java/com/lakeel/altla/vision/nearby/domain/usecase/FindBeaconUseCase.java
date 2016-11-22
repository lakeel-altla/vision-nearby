package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository mRepository;

    @Inject
    FindBeaconUseCase() {
    }

    public Single<BeaconsEntity> execute(String beaconId) {
        return mRepository.findBeaconById(beaconId);
    }
}
