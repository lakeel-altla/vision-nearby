package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserFavoriteRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindFavoritesUseCase {

    @Inject
    FirebaseUserFavoriteRepository favoritesRepository;

    @Inject
    FirebaseUserProfileRepository usersRepository;

    @Inject
    FindFavoritesUseCase() {
    }

    public Observable<UserProfile> execute() {
        String userId = MyUser.getUserId();
        return favoritesRepository.findFavorites(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(this::findUser);
    }

    private Observable<UserProfile> findUser(String userId) {
        return usersRepository.findUserProfile(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
