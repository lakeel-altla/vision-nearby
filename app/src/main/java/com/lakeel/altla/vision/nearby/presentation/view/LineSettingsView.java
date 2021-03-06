package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface LineSettingsView {

    void showLineUrl(@NonNull String url);

    void showSnackBar(@StringRes int resId);
}