package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

public final class Information {

    @Exclude
    public String userId;

    @Exclude
    public String informationId;

    public String title;

    public String body;

    public Object postTime;
}