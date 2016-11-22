package com.lakeel.altla.vision.nearby.data.repository;

import com.google.firebase.auth.FirebaseAuth;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseAuthRepository;

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
