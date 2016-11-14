package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.ItemModel;
import com.lakeel.profile.notification.presentation.presenter.model.PresencesModel;

import android.support.annotation.StringRes;

public interface FavoritesUserActivityView {

    void showSnackBar(@StringRes int resId);

    void showPresence(PresencesModel model);

    void showProfile(ItemModel model);

    void showTitle(String title);

    void showShareSheet();

    void initializeOptionMenu();

    void showLineUrl(String url);
}
