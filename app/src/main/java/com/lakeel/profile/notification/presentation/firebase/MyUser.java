package com.lakeel.profile.notification.presentation.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.lakeel.profile.notification.data.execption.UserNotAuthorizedException;

import android.net.Uri;

public final class MyUser {

    public static final class UserData {

        public String mUid;

        public String mDisplayName;

        public String mEmail;

        public String mImageUri;
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

        userData.mUid = myUser.getUid();
        userData.mDisplayName = myUser.getDisplayName();
        userData.mEmail = myUser.getEmail();

        Uri uri = myUser.getPhotoUrl();
        if (uri != null) {
            userData.mImageUri = uri.toString();
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
