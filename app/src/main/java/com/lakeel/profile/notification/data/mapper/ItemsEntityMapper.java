package com.lakeel.profile.notification.data.mapper;

import com.lakeel.profile.notification.data.MyUser;
import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.presentation.FirebaseUserData;

public final class ItemsEntityMapper {

    public ItemsEntity map() {
        FirebaseUserData user = MyUser.getUserData();

        ItemsEntity entity = new ItemsEntity();
        entity.name = user.mDisplayName;
        if (user.mImageUri != null) {
            entity.imageUri = user.mImageUri;
        }
        entity.email = user.mEmail;

        return entity;
    }
}
