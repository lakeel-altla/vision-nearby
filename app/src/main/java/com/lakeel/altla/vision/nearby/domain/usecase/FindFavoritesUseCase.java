package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.FavoritesEntity;
import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoriteRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.FavoriteModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class FindFavoritesUseCase {

    @Inject
    FirebaseFavoriteRepository mFavoritesRepository;

    @Inject
    FirebaseItemsRepository mFirebaseItemsRepository;

    private FavoriteModelMapper mFavoriteModelMapper = new FavoriteModelMapper();

    @Inject
    FindFavoritesUseCase() {
    }

    public Observable<FavoriteModel> execute() {
        return mFavoritesRepository
                .findFavorites()
                .flatMap(new Func1<FavoritesEntity, Observable<FavoriteModel>>() {

                    @Override
                    public Observable<FavoriteModel> call(FavoritesEntity favoritesEntity) {
                        Single<FavoritesEntity> favoritesSingle = Single.just(favoritesEntity);

                        Single<ItemsEntity> itemSingle = findItemById(favoritesEntity.key)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io());

                        return Single.zip(itemSingle, favoritesSingle, (itemsEntity1, favoritesEntity1) ->
                                mFavoriteModelMapper.map(itemsEntity1, favoritesEntity1)).toObservable();
                    }
                });
    }

    Single<ItemsEntity> findItemById(String id) {
        return mFirebaseItemsRepository.findItemsById(id);
    }
}
