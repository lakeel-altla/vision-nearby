package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.domain.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class SaveUsersToCmFavoritesUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    RestCmRepository mRestCmRepository;

    private CmFavoritesDataMapper mMapper = new CmFavoritesDataMapper();

    @Inject
    SaveUsersToCmFavoritesUseCase() {
    }

    public Observable<Timestamp> execute(List<String> ids) {
        return findCmUserIds(ids)
                .map(userIds -> mMapper.map(userIds))
                .flatMap(new Func1<CmFavoritesData, Observable<Timestamp>>() {
                    @Override
                    public Observable<Timestamp> call(CmFavoritesData data) {
                        return mRestCmRepository.saveFavorites(data).subscribeOn(Schedulers.io());
                    }
                });
    }

    Observable<List<String>> findCmUserIds(List<String> ids) {
        return Observable
                .from(ids)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String itemId) {
                        return mFirebaseCMLinksRepository.findCmJidByItemId(itemId).toObservable();
                    }
                })
                .toList();
    }
}
