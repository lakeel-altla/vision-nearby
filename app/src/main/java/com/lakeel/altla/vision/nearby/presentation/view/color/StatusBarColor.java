package com.lakeel.altla.vision.nearby.presentation.view.color;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

public final class StatusBarColor {

    private final Activity activity;

    private final int colorInt;

    public StatusBarColor(Activity activity, @ColorRes int colorInt) {
        this.activity = activity;
        this.colorInt = colorInt;
    }

    public void draw() {
        activity.getWindow().setStatusBarColor(
                ContextCompat.getColor(activity.getApplicationContext(), colorInt));
    }
}
