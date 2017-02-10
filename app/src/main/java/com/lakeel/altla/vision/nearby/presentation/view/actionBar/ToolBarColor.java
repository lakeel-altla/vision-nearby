package com.lakeel.altla.vision.nearby.presentation.view.actionBar;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public final class ToolBarColor implements BarColor {

    private final Activity activity;

    private final int primaryColorInt;

    private final int primaryDarkColorInt;

    ToolBarColor(Activity activity, @ColorRes int primaryColorInt, @ColorRes int primaryDarkColorInt) {
        this.activity = activity;
        this.primaryColorInt = primaryColorInt;
        this.primaryDarkColorInt = primaryDarkColorInt;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw() {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(
                            ContextCompat.getColor(activity.getApplicationContext(), primaryColorInt)));
        }

        activity.getWindow().setStatusBarColor(
                ContextCompat.getColor(activity.getApplicationContext(), primaryDarkColorInt));
    }
}
