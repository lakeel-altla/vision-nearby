package com.lakeel.altla.vision.nearby.presentation.presenter.favorite;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoritesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
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
    AnalyticsReporter analyticsReporter;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindFavoritesUseCase findFavoritesUseCase;

    @Inject
    RemoveFavoriteUseCase removeFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteListPresenter.class);

    private FavoriteModelMapper favoriteModelMapper = new FavoriteModelMapper();

    private final List<FavoriteModel> favoriteModels = new ArrayList<>();

    @Inject
    FavoriteListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findFavoritesUseCase
                .execute(MyUser.getUid())
                .filter(entity -> entity != null)
                .flatMap(entity -> findUser(entity.userId))
                .map(entity -> favoriteModelMapper.map(entity))
                .toList()
                .subscribe(favoritesModels -> {
                    favoriteModels.clear();
                    favoriteModels.addAll(favoritesModels);

                    getView().updateItems(favoriteModels);
                }, e -> {
                    getView().showSnackBar(R.string.error_process);
                });
        subscriptions.add(subscription);
    }

    public void onCreateItemView(FavoriteItemView favoriteItemView) {
        FavoritesItemPresenter favoritesItemPresenter = new FavoritesItemPresenter();
        favoritesItemPresenter.onCreateItemView(favoriteItemView);
        favoriteItemView.setItemPresenter(favoritesItemPresenter);
    }

    public final class FavoritesItemPresenter extends BaseItemPresenter<FavoriteItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(favoriteModels.get(position));
        }

        public void onClick(FavoriteModel model) {
            String userId = model.userId;
            String userName = model.name;
            getView().showFavoritesUserActivity(userId, userName);
        }

        public void onRemove(FavoriteModel model) {
            analyticsReporter.removeFavorite(model.userId, model.name);

            Subscription subscription = removeFavoriteUseCase
                    .execute(MyUser.getUid(), model.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                                LOGGER.error("Failed to remove favorite.", e);
                                getView().showSnackBar(R.string.error_not_deleted);
                            },
                            () -> {
                                int size = favoriteModels.size();
                                favoriteModels.remove(model);

                                if (CollectionUtils.isEmpty(favoriteModels)) {
                                    getView().removeAll(size);
                                } else {
                                    getView().updateItems(favoriteModels);
                                }

                                getView().showSnackBar(R.string.message_removed);
                            });
            subscriptions.add(subscription);
        }
    }

    private Observable<UserEntity> findUser(String userId) {
        return findUserUseCase.execute(userId).toObservable();
    }
}
