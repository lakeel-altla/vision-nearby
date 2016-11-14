package com.lakeel.profile.notification.data.mapper;

import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.data.entity.ItemsEntity;

public final class ItemsEntityMapper {

    public ItemsEntity map() {
        MyUser.UserData user = MyUser.getUserData();

        ItemsEntity entity = new ItemsEntity();
        entity.name = user.mDisplayName;
        if (user.mImageUri != null) {
            entity.imageUri = user.mImageUri;
        }
        entity.email = user.mEmail;

        return entity;
    }
}
