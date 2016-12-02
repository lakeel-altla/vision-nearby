package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import java.util.HashMap;
import java.util.Map;

public final class UserEntityMapper {

    public Map<String, Object> map() {
        Map<String, Object> map = new HashMap<>();

        MyUser.UserData user = MyUser.getUserData();

        map.put("name", user.displayName);
        map.put("imageUri", user.imageUri);
        map.put("email", user.email);

        return map;
    }
}
