package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveDeviceTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    FirebaseUserDeviceTokenRepository tokensRepository;

    @Inject
    SaveDeviceTokenUseCase() {
    }

    public Single<String> execute(String beaconId, String token) {
        String userId = MyUser.getUserId();
        return tokensRepository.saveDeviceToken(userId, beaconId, token).subscribeOn(Schedulers.io());
    }
}
