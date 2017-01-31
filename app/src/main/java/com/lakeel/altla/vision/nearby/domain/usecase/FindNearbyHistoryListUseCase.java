package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistoryUserProfile;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindNearbyHistoryListUseCase {

    @Inject
    FirebaseUserNearbyHistoryRepository historyRepository;

    @Inject
    FirebaseUserProfileRepository usersRepository;

    @Inject
    FindNearbyHistoryListUseCase() {
    }

    public Observable<NearbyHistoryUserProfile> execute() {
        String userId = MyUser.getUserId();
        return historyRepository.findNearbyHistoryList(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(history -> {
                    Observable<NearbyHistory> observable = Observable.just(history);
                    Observable<UserProfile> observable1 = findUser(history.userId);
                    return Observable.zip(observable, observable1, NearbyHistoryUserProfile::new);
                });
    }

    private Observable<UserProfile> findUser(String userId) {
        return usersRepository.findUserProfile(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
