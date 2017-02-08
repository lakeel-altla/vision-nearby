package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.lakeel.altla.vision.nearby.data.entity.UserProfileEntity;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

public final class UserEntityMapper {

    public UserProfileEntity map() {
        UserProfileEntity entity = new UserProfileEntity();
        MyUser.UserProfile profile = MyUser.getUserProfile();
        entity.name = profile.name;
        entity.imageUri = profile.imageUri;
        entity.email = profile.email;
        return entity;
    }
}
