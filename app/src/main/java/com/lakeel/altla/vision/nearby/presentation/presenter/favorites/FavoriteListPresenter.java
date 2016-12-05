package com.lakeel.altla.vision.nearby.presentation.presenter.favorites;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoritesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.FavoriteModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class FavoriteListPresenter extends BasePresenter<FavoriteListView> {

    @Inject
    FindUserUseCase mFindUserUseCase;

    @Inject
    FindFavoritesUseCase mFindFavoritesUseCase;

    @Inject
    RemoveFavoriteUseCase mRemoveFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteListPresenter.class);

    private FavoriteModelMapper mFavoriteModelMapper = new FavoriteModelMapper();

    private final List<FavoriteModel> mFavoriteModels = new ArrayList<>();

    @Inject
    FavoriteListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindFavoritesUseCase
                .execute(MyUser.getUid())
                .flatMap(entity -> {
                    String userId = entity.key;
                    Observable<UserEntity> itemsSingle = mFindUserUseCase.execute(userId).toObservable();

                    return Observable.zip(Observable.just(entity), itemsSingle, (favoritesEntity, itemsEntity) ->
                            mFavoriteModelMapper.map(favoritesEntity, itemsEntity));
                })
                .toList()
                .subscribe(favoritesModels -> {
                    mFavoriteModels.clear();
                    mFavoriteModels.addAll(favoritesModels);
                    getView().updateItems(mFavoriteModels);
                }, e -> {
                    getView().showSnackBar(R.string.error_process);
                });

        reusableCompositeSubscription.add(subscription);
    }

    public void onCreateItemView(FavoriteItemView favoriteItemView) {
        FavoritesItemPresenter favoritesItemPresenter = new FavoritesItemPresenter();
        favoritesItemPresenter.onCreateItemView(favoriteItemView);
        favoriteItemView.setItemPresenter(favoritesItemPresenter);
    }

    public final class FavoritesItemPresenter extends BaseItemPresenter<FavoriteItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mFavoriteModels.get(position));
        }

        public void onClick(FavoriteModel model) {
            String userId = model.userId;
            String userName = model.name;
            getView().showFavoritesUserActivity(userId, userName);
        }

        public void onRemove(FavoriteModel favoriteModel) {
            Subscription subscription = mRemoveFavoriteUseCase.execute(MyUser.getUid(), favoriteModel.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(throwable -> {
                                LOGGER.error("Failed to remove favorite", throwable);
                                getView().showSnackBar(R.string.error_not_deleted);
                            },
                            () -> {
                                int size = mFavoriteModels.size();
                                mFavoriteModels.remove(favoriteModel);

                                if (CollectionUtils.isEmpty(mFavoriteModels)) {
                                    getView().removeAll(size);
                                } else {
                                    getView().updateItems(mFavoriteModels);
                                }

                                getView().showSnackBar(R.string.message_removed);
                            });
            reusableCompositeSubscription.add(subscription);
        }
    }
}
