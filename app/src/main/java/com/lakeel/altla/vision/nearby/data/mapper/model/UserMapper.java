package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.model.User;

public final class UserMapper {

    public User map(UserEntity entity, String key) {
        User user = new User();
        user.userId = key;
        user.name = entity.name;
        user.email = entity.email;
        user.imageUri = entity.imageUri;
        return user;
    }
}
