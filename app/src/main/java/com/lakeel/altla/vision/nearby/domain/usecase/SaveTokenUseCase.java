package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseTokensRepository;
import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    FirebaseTokensRepository tokensRepository;

    @Inject
    SaveTokenUseCase() {
    }

    public Single<String> execute(String beaconId, String token) {
        String userId = MyUser.getUserId();
        return tokensRepository.save(userId, beaconId, token).subscribeOn(Schedulers.io());
    }
}
