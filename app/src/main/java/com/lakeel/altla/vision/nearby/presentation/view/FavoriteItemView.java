package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.favorites.FavoritesListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public interface FavoriteItemView extends ItemView {

    void setItemPresenter(FavoritesListPresenter.FavoritesItemPresenter itemPresenter);

    void showItem(FavoriteModel model);
}
