package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserFavoriteRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllFavoriteUseCase {

    @Inject
    UserFavoriteRepository favoritesRepository;

    @Inject
    UserProfileRepository usersRepository;

    @Inject
    FindAllFavoriteUseCase() {
    }

    public Observable<UserProfile> execute() {
        String userId = CurrentUser.getUid();

        return favoritesRepository.findAll(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(this::findUser);
    }

    private Observable<UserProfile> findUser(String userId) {
        return usersRepository.find(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
