package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.google.firebase.auth.FirebaseUser;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;

public final class ActivityModelMapper {

    private ActivityModelMapper() {
    }

    public static ActivityModel map() {
        ActivityModel model = new ActivityModel();
        FirebaseUser firebaseUser = CurrentUser.getUser();
        model.userId = firebaseUser.getUid();
        model.userName = firebaseUser.getDisplayName();
        model.email = firebaseUser.getEmail();
        if (firebaseUser.getPhotoUrl() != null) {
            model.imageUri = firebaseUser.getPhotoUrl().toString();
        }
        return model;
    }

    public static ActivityModel map(UserProfile userProfile) {
        ActivityModel model = new ActivityModel();
        model.userId = userProfile.userId;
        model.userName = userProfile.name;
        model.email = userProfile.email;
        model.imageUri = userProfile.imageUri;
        return model;
    }
}
