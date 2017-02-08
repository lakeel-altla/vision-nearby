package com.lakeel.altla.vision.nearby.presentation.analytics;

import android.os.Bundle;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

final class UserParam {

    private final Bundle bundle = new Bundle();

    UserParam() {
        MyUser.UserProfile userProfile = MyUser.getUserProfile();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), userProfile.userId);
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), userProfile.name);
    }

    void putString(String key, String value) {
        bundle.putString(key, value);
    }

    Bundle toBundle() {
        return bundle;
    }
}
