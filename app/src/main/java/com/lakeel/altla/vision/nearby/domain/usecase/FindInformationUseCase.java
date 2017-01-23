package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseInformationRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindInformationUseCase {

    @Inject
    FirebaseInformationRepository repository;

    @Inject
    FindInformationUseCase() {
    }

    public Single<InformationEntity> execute(String userId, String informationId) {
        return repository.find(userId, informationId);
    }
}
