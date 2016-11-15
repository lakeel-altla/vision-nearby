package com.lakeel.profile.notification.domain.usecase;


import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveUserBeaconUseCase {

    @Inject
    FirebaseItemsRepository mRepository;

    @Inject
    SaveUserBeaconUseCase() {
    }

    public Single<String> execute(String beaconId) {
        return mRepository.saveBeaconId(beaconId);
    }
}
