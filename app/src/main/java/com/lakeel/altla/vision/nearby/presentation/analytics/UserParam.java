package com.lakeel.altla.vision.nearby.presentation.analytics;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

final class UserParam {

    private final Bundle bundle = new Bundle();

    UserParam() {
        FirebaseUser firebaseUser = CurrentUser.getUser();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), firebaseUser.getUid());
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), firebaseUser.getDisplayName());
    }

    void putString(String key, String value) {
        bundle.putString(key, value);
    }

    Bundle toBundle() {
        return bundle;
    }
}
