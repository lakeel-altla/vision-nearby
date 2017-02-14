package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingUserModel;

public interface PassingUserView {

    void showTitle(String title);

    void updateModel(PassingUserModel model);

    void showLocation(String latitude, String longitude);

    void hideLocation();

    void showAddButton();

    void hideAddButton();

    void showSnackBar(@StringRes int resId);
}
