package com.lakeel.profile.notification.android;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lakeel.profile.notification.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public final class ConfirmDialog {

    private MaterialDialog.Builder mBuilder;

    public ConfirmDialog(@NonNull Context context) {
        mBuilder = new MaterialDialog.Builder(context);
        mBuilder.title(R.string.title_confirm);
        mBuilder.positiveText(R.string.dialog_button_positive);
    }

    public void setContent(@StringRes int resId) {
        mBuilder.content(resId);
    }

    public void show() {
        mBuilder.show();
    }
}
