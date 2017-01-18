package com.lakeel.altla.vision.nearby.presentation.firebase;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lakeel.altla.vision.nearby.presentation.exception.UserNotAuthorizedException;

public final class MyUser {

    public static final class UserData {

        public String userId;

        public String userName;

        public String email;

        public String imageUri;
    }

    private MyUser() {
    }

    public static boolean isAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static String getUid() {
        return getMyUser().getUid();
    }

    public static UserData getUserData() {
        FirebaseUser myUser = getMyUser();

        UserData userData = new UserData();

        userData.userId = myUser.getUid();
        userData.userName = myUser.getDisplayName();
        userData.email = myUser.getEmail();

        Uri uri = myUser.getPhotoUrl();
        if (uri != null) {
            userData.imageUri = uri.toString();
        }

        return userData;
    }

    private static FirebaseUser getMyUser() {
        FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
        if (myUser == null) {
            throw new UserNotAuthorizedException("Not found signed in user");
        }

        return myUser;
    }
}
