package com.lakeel.profile.notification.presentation.view;

import android.support.annotation.StringRes;

public interface LineSettingsView {

    void showLINEUrl(String url);

    void showSnackBar(@StringRes int resId);
}
