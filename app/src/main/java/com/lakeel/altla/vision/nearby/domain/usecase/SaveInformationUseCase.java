package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserInformationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Information;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveInformationUseCase {

    @Inject
    UserInformationRepository repository;

    @Inject
    SaveInformationUseCase() {
    }

    public Completable execute(@NonNull String userId, @NonNull String title, @NonNull String body) {
        Information information = new Information();
        information.userId = userId;
        information.title = title;
        information.body = body;
        information.postTime = ServerValue.TIMESTAMP;
        return repository.save(information).subscribeOn(Schedulers.io());
    }
}
