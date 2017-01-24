package com.lakeel.altla.vision.nearby.android;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lakeel.altla.vision.nearby.R;

public final class ConfirmDialog {

    private final MaterialDialog.Builder builder;

    public ConfirmDialog(@NonNull Activity activity, @StringRes int resId) {
        builder = new MaterialDialog.Builder(activity);
        builder.title(R.string.title_confirm);
        builder.positiveText(R.string.dialog_button_positive);
        builder.content(resId);
    }

    public void show() {
        builder.show();
    }
}
