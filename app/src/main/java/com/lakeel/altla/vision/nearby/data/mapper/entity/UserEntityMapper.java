package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

public final class UserEntityMapper {

    public UserEntity map() {
        UserEntity entity = new UserEntity();
        MyUser.UserProfile profile = MyUser.getUserProfile();
        entity.name = profile.userName;
        entity.imageUri = profile.imageUri;
        entity.email = profile.email;
        return entity;
    }
}
