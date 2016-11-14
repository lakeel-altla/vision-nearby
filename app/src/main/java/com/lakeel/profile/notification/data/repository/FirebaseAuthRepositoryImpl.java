package com.lakeel.profile.notification.data.repository;

import com.google.firebase.auth.FirebaseAuth;

import com.lakeel.profile.notification.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;

import rx.Completable;

public final class FirebaseAuthRepositoryImpl implements FirebaseAuthRepository {

    @Inject
    public FirebaseAuthRepositoryImpl() {
    }

    @Override
    public Completable signOut() {
        return Completable.create(subscriber -> {
            FirebaseAuth.getInstance().signOut();
            subscriber.onCompleted();
        });
    }
}
