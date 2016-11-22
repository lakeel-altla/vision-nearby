package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.domain.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class SaveUserToCmFavoritesUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    RestCmRepository mRestCmRepository;

    private CmFavoritesDataMapper mMapper = new CmFavoritesDataMapper();

    @Inject
    SaveUserToCmFavoritesUseCase() {
    }

    public Observable<Timestamp> execute(String itemId) {
        return findCmJidByItemId(itemId)
                .map(contactJid -> mMapper.map(contactJid))
                .flatMapObservable(new Func1<CmFavoritesData, Observable<Timestamp>>() {
                    @Override
                    public Observable<Timestamp> call(CmFavoritesData cmFavoritesData) {
                        return mRestCmRepository.saveFavorites(cmFavoritesData).subscribeOn(Schedulers.io());
                    }
                });
    }

    Single<String> findCmJidByItemId(String itemId) {
        return mFirebaseCMLinksRepository
                .findCmJidByItemId(itemId)
                .subscribeOn(Schedulers.io());
    }
}
