package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserPassingModel;

public interface PassingUserView {

    void showTitle(String title);

    void showProfile(UserPassingModel model);

    void showTimes(long times);

    void showPassingData(UserPassingModel model);

    void showPresence(UserPassingModel model);

    void showLocationMap(String latitude, String longitude);

    void hideLocation();

    void showAddButton();

    void hideAddButton();

    void showLineUrl(String url);

    void showSnackBar(@StringRes int resId);
}
