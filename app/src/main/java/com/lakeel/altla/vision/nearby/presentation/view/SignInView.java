package com.lakeel.altla.vision.nearby.presentation.view;


import android.content.Intent;
import android.support.annotation.StringRes;

public interface SignInView {

    void showSignInActivity(Intent intent);

    void signInSucceeded();

    void showSnackBar(@StringRes int resId);
}
