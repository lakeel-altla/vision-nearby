package com.lakeel.altla.vision.nearby.presentation.firebase;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class MyUser {

    public static final class UserProfile {

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

    public static String getUserId() {
        return getMyUser().getUid();
    }

    public static UserProfile getUserProfile() {
        FirebaseUser myUser = getMyUser();

        UserProfile userProfile = new UserProfile();

        userProfile.userId = myUser.getUid();
        userProfile.userName = myUser.getDisplayName();
        userProfile.email = myUser.getEmail();

        Uri uri = myUser.getPhotoUrl();
        if (uri != null) {
            userProfile.imageUri = uri.toString();
        }

        return userProfile;
    }

    private static FirebaseUser getMyUser() {
        FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
        if (myUser == null) {
            throw new UserNotAuthorizedException("Not found signed in user.");
        }

        return myUser;
    }
}
