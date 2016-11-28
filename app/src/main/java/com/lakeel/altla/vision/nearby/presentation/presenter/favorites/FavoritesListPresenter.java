package com.lakeel.altla.vision.nearby.presentation.presenter.favorites;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoritesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindItemUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class FavoritesListPresenter extends BasePresenter<FavoriteListView> {

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    FindFavoritesUseCase mFindFavoritesUseCase;

    @Inject
    RemoveFavoriteUseCase mRemoveFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoritesListPresenter.class);

    private final List<FavoriteModel> mFavoriteModels = new ArrayList<>();

    @Inject
    FavoritesListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindFavoritesUseCase
                .execute()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
            String userId = model.mId;
            String userName = model.mName;
            getView().showFavoritesUserActivity(userId, userName);
        }

        public void onRemove(FavoriteModel favoriteModel) {
            Subscription subscription = mRemoveFavoriteUseCase.execute(favoriteModel.mId)
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
