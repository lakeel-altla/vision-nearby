package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveDeviceTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    UserDeviceTokenRepository tokensRepository;

    @Inject
    SaveDeviceTokenUseCase() {
    }

    public Single<String> execute(String beaconId, String token) {
        String userId = MyUser.getUserId();
        return tokensRepository.save(userId, beaconId, token).subscribeOn(Schedulers.io());
    }
}
