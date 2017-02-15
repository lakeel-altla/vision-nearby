package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.domain.model.PassingUserProfile;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository historyRepository;

    @Inject
    UserProfileRepository usersRepository;

    @Inject
    FindAllNearbyHistoryUseCase() {
    }

    public Observable<PassingUserProfile> execute() {
        String userId = CurrentUser.getUid();

        return historyRepository.findAll(userId)
                .subscribeOn(Schedulers.io())
                // Join the data.
                .flatMap(nearbyHistory -> {
                    Observable<NearbyHistory> observable = Observable.just(nearbyHistory);
                    Observable<UserProfile> observable1 = findUser(nearbyHistory.userId);
                    return Observable.zip(observable, observable1, PassingUserProfile::new);
                });
    }

    private Observable<UserProfile> findUser(String userId) {
        return usersRepository.find(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
