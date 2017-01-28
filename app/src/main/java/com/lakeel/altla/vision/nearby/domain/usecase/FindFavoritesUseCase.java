package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseFavoritesRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindFavoritesUseCase {

    @Inject
    FirebaseFavoritesRepository favoritesRepository;

    @Inject
    FirebaseUsersRepository usersRepository;

    @Inject
    FindFavoritesUseCase() {
    }

    public Observable<User> execute() {
        String userId = MyUser.getUserId();
        return favoritesRepository.findFavorites(userId).subscribeOn(Schedulers.io())
                .flatMap(entity -> findUser(userId));
    }

    private Observable<User> findUser(String userId) {
        return usersRepository.findUser(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
