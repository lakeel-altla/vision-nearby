package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindAllPassingTimeUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    FindAllPassingTimeUseCase() {
    }

    public Single<Long> execute(@NonNull String passingUserId) {
        String userId = CurrentUser.getUid();
        return repository.findPassingTimes(userId, passingUserId).subscribeOn(Schedulers.io());
    }
}
