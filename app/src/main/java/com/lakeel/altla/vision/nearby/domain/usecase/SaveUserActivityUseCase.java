package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserActivityUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveUserActivityUseCase() {
    }

    public Completable execute(String uniqueId, DetectedActivity userActivity) {
        String userId = MyUser.getUserId();
        return repository.saveUserActivity(uniqueId, userId, userActivity).subscribeOn(Schedulers.io());
    }
}
