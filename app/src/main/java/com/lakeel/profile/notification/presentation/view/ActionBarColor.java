package com.lakeel.profile.notification.presentation.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public final class ActionBarColor {

    private Activity mActivity;

    private int mColorInt;

    public ActionBarColor(Activity activity, @ColorRes int colorInt) {
        mActivity = activity;
        mColorInt = colorInt;
    }

    public void draw() {
        if (Build.VERSION.SDK_INT >= 21) {
            mActivity.getWindow().setStatusBarColor(
                    ContextCompat.getColor(mActivity.getApplicationContext(), mColorInt));
        }

        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(
                            ContextCompat.getColor(mActivity.getApplicationContext(), mColorInt)));
        }
    }
}
