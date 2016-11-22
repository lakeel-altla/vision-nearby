package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindUserBeaconsUseCase {

    @Inject
    FirebaseBeaconsRepository mRepository;

    @Inject
    FindUserBeaconsUseCase() {
    }

    public Observable<BeaconsEntity> execute(String userId) {
        return mRepository.findBeaconsByUserId(userId);
    }
}
