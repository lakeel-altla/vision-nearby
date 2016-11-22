package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

import java.util.List;

public interface FavoriteListView extends BaseView {

    void updateItems(List<FavoriteModel> models);

    void removeAll(@IntRange(from = 0) int size);

    void showSnackBar(@StringRes int resId);

    void showFavoritesUserActivity(String id);
}
