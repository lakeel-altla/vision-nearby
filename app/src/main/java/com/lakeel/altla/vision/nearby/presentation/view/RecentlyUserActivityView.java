package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.ItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;

import android.support.annotation.StringRes;

public interface RecentlyUserActivityView {

    void showTitle(String title);

    void showSnackBar(@StringRes int resId);

    void showPresence(PresenceModel model);

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
