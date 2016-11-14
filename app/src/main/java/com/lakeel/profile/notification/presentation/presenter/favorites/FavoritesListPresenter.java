package com.lakeel.profile.notification.presentation.presenter.favorites;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.core.CollectionUtils;
import com.lakeel.profile.notification.domain.usecase.FindFavoritesUseCase;
import com.lakeel.profile.notification.domain.usecase.FindItemUseCase;
import com.lakeel.profile.notification.domain.usecase.RemoveFavoriteUseCase;
import com.lakeel.profile.notification.presentation.presenter.BaseItemPresenter;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.FavoriteModelMapper;
import com.lakeel.profile.notification.presentation.presenter.model.FavoriteModel;
import com.lakeel.profile.notification.presentation.view.FavoriteItemView;
import com.lakeel.profile.notification.presentation.view.FavoriteListView;

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
        getView().showTitle();

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
        mCompositeSubscription.add(subscription);
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
            String id = model.mId;
            getView().showFavoritesUserActivity(id);
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
            mCompositeSubscription.add(subscription);
        }
    }
}
