package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;
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

    public Single<String> execute(String token) {
        String userId = MyUser.getUserId();
        return preferenceRepository.findBeaconId(userId)
                .flatMap(beaconId -> saveToken(userId, beaconId, token))
                .subscribeOn(Schedulers.io());
    }

    private Single<String> saveToken(String userId, String beaconId, String token) {
        return tokensRepository.saveToken(userId, beaconId, token);
    }
}
