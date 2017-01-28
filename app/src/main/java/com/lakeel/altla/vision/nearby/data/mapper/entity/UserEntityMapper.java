package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import java.util.HashMap;
import java.util.Map;

public final class UserEntityMapper {

    public Map<String, Object> map() {
        Map<String, Object> map = new HashMap<>();

        MyUser.UserProfile user = MyUser.getUserData();

        map.put("userName", user.userName);
        map.put("imageUri", user.imageUri);
        map.put("email", user.email);

        return map;
    }
}
