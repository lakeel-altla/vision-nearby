package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

import java.util.List;

public interface FavoriteListView {

    void updateItems(List<FavoriteModel> models);

    void removeAll(@IntRange(from = 0) int size);

    void showEmptyView();

    void hideEmptyView();

    void showSnackBar(@StringRes int resId);

    void showFavoriteUserFragment(FavoriteModel model);
}
