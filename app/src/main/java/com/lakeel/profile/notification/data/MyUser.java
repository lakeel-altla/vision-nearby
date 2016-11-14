package com.lakeel.profile.notification.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.lakeel.profile.notification.data.execption.UserNotFoundException;
import com.lakeel.profile.notification.presentation.FirebaseUserData;

import android.net.Uri;

public final class MyUser {

    private MyUser() {
    }

    public static boolean isAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static FirebaseUser getMyUser() {
        FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
        if (myUser == null) {
            throw new UserNotFoundException("Not found signed in user");
        }

        return myUser;
    }

    public static String getUid() {
        return getMyUser().getUid();
    }

    public static FirebaseUserData getUserData() {
        FirebaseUser myUser = getMyUser();

        FirebaseUserData userData = new FirebaseUserData();

        userData.mUid = myUser.getUid();
        userData.mDisplayName = myUser.getDisplayName();
        userData.mEmail = myUser.getEmail();

        Uri uri = myUser.getPhotoUrl();
        if (uri != null) {
            userData.mImageUri = uri.toString();
        }

        return userData;
    }
}
