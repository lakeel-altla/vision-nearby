package com.lakeel.altla.vision.nearby.presentation.view.bar;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public final class ActionBarColor {

    private final Activity mActivity;

    private final int mColorInt;

    public ActionBarColor(Activity activity, @ColorRes int colorInt) {
        mActivity = activity;
        mColorInt = colorInt;
    }

    public void draw() {
        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(
                            ContextCompat.getColor(mActivity.getApplicationContext(), mColorInt)));
        }
    }
}
