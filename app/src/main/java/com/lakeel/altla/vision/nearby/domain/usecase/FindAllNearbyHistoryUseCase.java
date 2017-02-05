package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistoryUserProfile;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

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
        return usersRepository.find(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
