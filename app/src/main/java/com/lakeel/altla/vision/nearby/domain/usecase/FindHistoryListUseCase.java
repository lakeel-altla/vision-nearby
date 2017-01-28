package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.domain.model.History;
import com.lakeel.altla.vision.nearby.domain.model.HistoryUser;
import com.lakeel.altla.vision.nearby.domain.model.User;
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

    public Observable<HistoryUser> execute() {
        String userId = MyUser.getUserId();
        return historyRepository.findHistoryList(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(history -> {
                    Observable<History> observable = Observable.just(history);
                    Observable<User> observable1 = findUser(history.userId);
                    return Observable.zip(observable, observable1, HistoryUser::new);
                });
    }

    private Observable<User> findUser(String userId) {
        return usersRepository.findUser(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
