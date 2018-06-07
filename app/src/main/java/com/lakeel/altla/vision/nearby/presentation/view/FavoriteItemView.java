package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.presenter.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public interface FavoriteItemView extends ItemView {

    void setItemPresenter(@NonNull FavoriteListPresenter.FavoritesItemPresenter itemPresenter);

    void showItem(@NonNull FavoriteModel model);
}
