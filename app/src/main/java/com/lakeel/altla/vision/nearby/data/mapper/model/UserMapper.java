package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.UserProfileEntity;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;

public final class UserMapper {

    public UserProfile map(UserProfileEntity entity, String key) {
        UserProfile userProfile = new UserProfile();
        userProfile.userId = key;
        userProfile.name = entity.name;
        userProfile.email = entity.email;
        userProfile.imageUri = entity.imageUri;
        return userProfile;
    }
}
