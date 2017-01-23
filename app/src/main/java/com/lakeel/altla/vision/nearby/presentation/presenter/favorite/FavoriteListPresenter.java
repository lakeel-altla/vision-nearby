package com.lakeel.altla.vision.nearby.presentation.presenter.favorite;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoritesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.FavoriteModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class FavoriteListPresenter extends BasePresenter<FavoriteListView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindFavoritesUseCase findFavoritesUseCase;

    @Inject
    RemoveFavoriteUseCase removeFavoriteUseCase;

    private FavoriteModelMapper favoriteModelMapper = new FavoriteModelMapper();

    private final List<FavoriteModel> favoriteModels = new ArrayList<>();

    @Inject
    FavoriteListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findFavoritesUseCase.execute()
                .map(entity -> favoriteModelMapper.map(entity))
                .toList()
                .subscribe(favoritesModels -> {
                    favoriteModels.clear();
                    favoriteModels.addAll(favoritesModels);

                    if (CollectionUtils.isEmpty(favoritesModels)) {
                        getView().showEmptyView();
                    } else {
                        getView().hideEmptyView();
                    }

                    getView().updateItems(favoriteModels);
                }, new ErrorAction<>());
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
            getView().showFavoriteUserFragment(model);
        }

        public void onRemove(FavoriteModel model) {
            analyticsReporter.removeFavorite(model.userId, model.userName);

            Subscription subscription = removeFavoriteUseCase.execute(model.userId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorAction<>(),
                            () -> {
                                int size = favoriteModels.size();
                                favoriteModels.remove(model);

                                if (CollectionUtils.isEmpty(favoriteModels)) {
                                    getView().removeAll(size);
                                    getView().showEmptyView();
                                } else {
                                    getView().hideEmptyView();
                                    getView().updateItems(favoriteModels);
                                }

                                getView().showSnackBar(R.string.message_removed);
                            });
            subscriptions.add(subscription);
        }
    }
}
