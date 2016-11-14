package com.lakeel.profile.notification.presentation.view;


import android.content.Intent;
import android.support.annotation.StringRes;

public interface SignInView extends BaseView {

    void showSignInActivity(Intent intent);

    void showTitle(String title);

    void onSignedIn();

    void showSnackBar(@StringRes int resId);
}
