package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.data.entity.RecentlyEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseRecentlyRepository;
import com.lakeel.profile.notification.presentation.presenter.mapper.RecentlyItemModelMapper;
import com.lakeel.profile.notification.presentation.presenter.model.RecentlyItemModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class FindRecentlyUseCase {

    @Inject
    FirebaseRecentlyRepository mFirebaseRecentlyRepository;

    @Inject
    FirebaseItemsRepository mFirebaseItemsRepository;

    private RecentlyItemModelMapper mRecentlyItemModelMapper = new RecentlyItemModelMapper();

    @Inject
    FindRecentlyUseCase() {
    }

    public Observable<RecentlyItemModel> execute() {
        return mFirebaseRecentlyRepository
                .findRecently()
                .flatMap(new Func1<RecentlyEntity, Observable<RecentlyItemModel>>() {
                    @Override
                    public Observable<RecentlyItemModel> call(RecentlyEntity entity) {
                        Single<RecentlyEntity> recentlySingle = Single.just(entity);

                        Single<ItemsEntity> itemSingle = findItemById(entity.id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io());

                        return Single.zip(itemSingle, recentlySingle, (itemsEntity1, recentlyEntity1) ->
                                mRecentlyItemModelMapper.map(itemsEntity1, recentlyEntity1)
                        ).toObservable();
                    }
                });
    }

    Single<ItemsEntity> findItemById(String id) {
        return mFirebaseItemsRepository.findItemsById(id);
    }
}
