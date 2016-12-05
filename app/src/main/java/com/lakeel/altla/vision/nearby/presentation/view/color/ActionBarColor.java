package com.lakeel.altla.vision.nearby.presentation.view.color;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public final class ActionBarColor {

    private final Activity activity;

    private final int colorInt;

    public ActionBarColor(Activity activity, @ColorRes int colorInt) {
        this.activity = activity;
        this.colorInt = colorInt;
    }

    public void draw() {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(
                            ContextCompat.getColor(activity.getApplicationContext(), colorInt)));
        }
    }
}
