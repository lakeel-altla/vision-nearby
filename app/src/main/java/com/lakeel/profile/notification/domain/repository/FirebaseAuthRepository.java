package com.lakeel.profile.notification.domain.repository;

import rx.Completable;

public interface FirebaseAuthRepository {

    Completable signOut();
}
