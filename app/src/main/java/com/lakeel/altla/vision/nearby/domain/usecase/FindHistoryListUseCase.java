package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryUserEntity;
import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindHistoryListUseCase {

    @Inject
    FirebaseHistoryRepository historyRepository;

    @Inject
    FirebaseUsersRepository usersRepository;

    @Inject
    FindHistoryListUseCase() {
    }

    public Observable<HistoryUserEntity> execute() {
        String userId = MyUser.getUserId();
        return historyRepository.findHistoryList(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(entity -> {
                    Observable<HistoryEntity> observable = Observable.just(entity);
                    Observable<UserEntity> observable1 = findUser(entity.userId);
                    return Observable.zip(observable, observable1, HistoryUserEntity::new);
                });
    }

    private Observable<UserEntity> findUser(String userId) {
        return usersRepository.findUserByUserId(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
