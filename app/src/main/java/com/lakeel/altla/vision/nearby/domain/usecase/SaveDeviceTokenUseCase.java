package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveDeviceTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    UserDeviceTokenRepository tokensRepository;

    @Inject
    SaveDeviceTokenUseCase() {
    }

    public Completable execute(String beaconId) {
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.userId = CurrentUser.getUid();
        deviceToken.beaconId = beaconId;
        deviceToken.token = FirebaseInstanceId.getInstance().getToken();
        return tokensRepository.save(deviceToken).subscribeOn(Schedulers.io());
    }
}
