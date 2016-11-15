package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.BeaconsEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseBeaconsRepository;

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
