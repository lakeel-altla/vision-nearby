package com.lakeel.altla.vision.nearby.presentation.view;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

public final class StatusBarColor {

    private final Activity mActivity;

    private final int mColorInt;

    public StatusBarColor(Activity activity, @ColorRes int colorInt) {
        mActivity = activity;
        mColorInt = colorInt;
    }

    public void draw() {
        mActivity.getWindow().setStatusBarColor(
                ContextCompat.getColor(mActivity.getApplicationContext(), mColorInt));
    }
}
