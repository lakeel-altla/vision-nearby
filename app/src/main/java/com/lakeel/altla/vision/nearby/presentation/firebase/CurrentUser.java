package com.lakeel.altla.vision.nearby.presentation.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class CurrentUser {

    private CurrentUser() {
    }

    public static String getUid() {
        FirebaseUser firebaseUser = getUser();
        return firebaseUser.getUid();
    }

    public static FirebaseUser getUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            throw new IllegalStateException("Not found signed in user.");
        }
        return firebaseUser;
    }
}
