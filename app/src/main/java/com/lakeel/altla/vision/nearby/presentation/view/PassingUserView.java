package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingUserModel;

public interface PassingUserView {

    void showTitle(String title);

    void showProfile(PassingUserModel model);

    void showTimes(long times);

    void showPassingData(PassingUserModel model);

    void showPresence(PassingUserModel model);

    void showLocationMap(String latitude, String longitude);

    void hideLocation();

    void showAddButton();

    void hideAddButton();

    void showLineUrl(String url);

    void showSnackBar(@StringRes int resId);
}
