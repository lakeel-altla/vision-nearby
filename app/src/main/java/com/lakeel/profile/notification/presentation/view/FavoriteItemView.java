package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.favorites.FavoritesListPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.FavoriteModel;

public interface FavoriteItemView extends ItemView {

    void setItemPresenter(FavoritesListPresenter.FavoritesItemPresenter itemPresenter);

    void showItem(FavoriteModel model);
}
