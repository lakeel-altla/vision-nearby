package com.lakeel.altla.vision.nearby.presentation.view.actionBar;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

final class ActionBarColor implements BarColor {

    private final Activity activity;

    private final int colorInt;

    ActionBarColor(Activity activity, @ColorRes int colorInt) {
        this.activity = activity;
        this.colorInt = colorInt;
    }

    @Override
    public void draw() {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(
                            ContextCompat.getColor(activity.getApplicationContext(), colorInt)));
        }
    }
}
