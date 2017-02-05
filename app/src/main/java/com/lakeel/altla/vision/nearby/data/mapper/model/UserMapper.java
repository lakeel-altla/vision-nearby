package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.data.entity.UserProfileEntity;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;

public final class UserMapper {

    public UserProfile map(DataSnapshot snapshot) {
        UserProfileEntity entity = snapshot.getValue(UserProfileEntity.class);

        UserProfile userProfile = new UserProfile();
        userProfile.userId = snapshot.getKey();
        userProfile.name = entity.name;
        userProfile.email = entity.email;
        userProfile.imageUri = entity.imageUri;

        return userProfile;
    }
}
