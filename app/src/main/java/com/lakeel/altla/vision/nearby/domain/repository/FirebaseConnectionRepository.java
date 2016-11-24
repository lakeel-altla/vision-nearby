package com.lakeel.altla.vision.nearby.domain.repository;

import rx.Observable;

public interface FirebaseConnectionRepository {

    Observable<Object> observeConnected();
}
