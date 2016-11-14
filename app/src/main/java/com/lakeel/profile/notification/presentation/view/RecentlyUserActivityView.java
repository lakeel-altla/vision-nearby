package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.ItemModel;
import com.lakeel.profile.notification.presentation.presenter.model.PresencesModel;

import android.content.Intent;
import android.support.annotation.StringRes;

public interface RecentlyUserActivityView {

    void showTitle(String title);

    void showSnackBar(@StringRes int resId);

    void showPresence(PresencesModel model);

    void showLocationMap(String latitude, String longitude);

    void showLocationText(String text);

    void hideLocation();

    void showTimes(long times);

    void showProfile(ItemModel model);

    void showAddButton();

    void hideAddButton();

    void initializeOptionMenu();

    void showLineUrl(String url);

    void showShareSheet();
}
