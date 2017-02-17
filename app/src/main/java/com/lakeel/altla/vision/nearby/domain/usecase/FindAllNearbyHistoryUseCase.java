package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistoryUserProfile;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository nearbyHistoryRepository;

    @Inject
    UserProfileRepository userProfileRepository;

    @Inject
    FindAllNearbyHistoryUseCase() {
    }

    public Observable<NearbyHistoryUserProfile> execute() {
        String userId = CurrentUser.getUid();

        return nearbyHistoryRepository.findAll(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(nearbyHistory -> Observable.zip(Observable.just(nearbyHistory), findUser(nearbyHistory.userId), NearbyHistoryUserProfile::new));
    }

    private Observable<UserProfile> findUser(String userId) {
        return userProfileRepository.find(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
