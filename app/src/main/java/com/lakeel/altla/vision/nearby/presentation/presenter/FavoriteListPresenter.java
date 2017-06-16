package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.FavoriteModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class FavoriteListPresenter extends BasePresenter<FavoriteListView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindAllFavoriteUseCase findAllFavoriteUseCase;

    @Inject
    RemoveFavoriteUseCase removeFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteListPresenter.class);

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private final List<FavoriteModel> models = new ArrayList<>();

    @Inject
    FavoriteListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findAllFavoriteUseCase
                .execute()
                .map(FavoriteModelMapper::map)
                .toList()
                .subscribe(favoritesModels -> {
                    models.clear();
                    models.addAll(favoritesModels);

                    if (CollectionUtils.isEmpty(favoritesModels)) {
                        getView().showEmptyView();
                    } else {
                        getView().hideEmptyView();
                    }

                    getView().updateItems(models);
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onCreateItemView(FavoriteItemView favoriteItemView) {
        FavoritesItemPresenter favoritesItemPresenter = new FavoritesItemPresenter();
        favoritesItemPresenter.onCreateItemView(favoriteItemView);
        favoriteItemView.setItemPresenter(favoritesItemPresenter);
    }

    public final class FavoritesItemPresenter extends BaseItemPresenter<FavoriteItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(models.get(position));
        }

        public void onClick(FavoriteModel model) {
            getView().showFavoriteUserFragment(model);
        }

        public void onRemove(FavoriteModel model) {
            analyticsReporter.removeFavorite(model.userId, model.userName);

            Subscription subscription = removeFavoriteUseCase
                    .execute(model.userId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                                LOGGER.error("Failed.", e);
                                getView().showSnackBar(R.string.snackBar_error_failed);
                            },
                            () -> {
                                int size = models.size();
                                models.remove(model);

                                if (CollectionUtils.isEmpty(models)) {
                                    getView().removeAll(size);
                                    getView().showEmptyView();
                                } else {
                                    getView().hideEmptyView();
                                    getView().updateItems(models);
                                }

                                getView().showSnackBar(R.string.snackBar_message_removed);
                            });
            subscriptions.add(subscription);
        }
    }
}
