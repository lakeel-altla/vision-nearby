package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseInformationRepository {

    Completable save(String userId, String title, String message);

    Observable<InformationEntity> findList(String userId);

    Single<InformationEntity> find(String userId, String informationId);
}
