package com.lakeel.altla.vision.nearby.presentation.analytics;

import android.os.Bundle;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

public final class UserParam {

    private final Bundle bundle = new Bundle();

    public UserParam() {
        MyUser.UserData userData = MyUser.getUserData();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), userData.userId);
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), userData.userName);
    }

    public void putString(String key, String value) {
        bundle.putString(key, value);
    }

    public Bundle toBundle() {
        return bundle;
    }
}
