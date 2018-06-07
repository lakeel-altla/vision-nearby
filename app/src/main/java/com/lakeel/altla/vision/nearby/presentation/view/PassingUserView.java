package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingUserModel;

public interface PassingUserView {

    void showTitle(@NonNull String title);

    void updateModel(@NonNull PassingUserModel model);

    void showLocation(@NonNull String latitude, @NonNull String longitude);

    void hideLocation();

    void showAddButton();

    void hideAddButton();

    void showSnackBar(@StringRes int resId);
}
