package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

public final class UserProfile {

    @Exclude
    public String userId;

    public String name;

    public String imageUri;

    public String email;
}