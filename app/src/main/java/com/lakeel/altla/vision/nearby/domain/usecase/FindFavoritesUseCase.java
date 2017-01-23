package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseFavoritesRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
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

    public Observable<UserEntity> execute() {
        String userId = MyUser.getUserId();
        return favoritesRepository.findFavoritesByUserId(userId).subscribeOn(Schedulers.io())
                .flatMap(entity -> findUser(entity.userId));
    }

    private Observable<UserEntity> findUser(String userId) {
        return usersRepository.findUserByUserId(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
