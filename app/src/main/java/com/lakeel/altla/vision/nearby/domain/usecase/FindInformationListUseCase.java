package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseInformationRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindInformationListUseCase {

    @Inject
    FirebaseInformationRepository repository;

    @Inject
    FindInformationListUseCase() {
    }

    public Observable<InformationEntity> execute(String userId) {
        return repository.findList(userId);
    }
}
