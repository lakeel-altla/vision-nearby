package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

public interface SettingsView extends BaseView {

    void showCMPreferences();

    void showSnackBar(@StringRes int resId);
}
