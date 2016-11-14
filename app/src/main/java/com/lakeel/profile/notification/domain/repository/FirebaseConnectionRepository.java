package com.lakeel.profile.notification.domain.repository;

import rx.Observable;

public interface FirebaseConnectionRepository {

    Observable<Object> observeConnectivity();
}
