package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public interface FavoriteItemView extends ItemView {

    void setItemPresenter(FavoriteListPresenter.FavoritesItemPresenter itemPresenter);

    void showItem(FavoriteModel model);
}
