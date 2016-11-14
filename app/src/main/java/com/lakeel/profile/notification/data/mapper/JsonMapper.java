package com.lakeel.profile.notification.data.mapper;

import com.google.gson.Gson;

public final class JsonMapper {

    private Gson mGoogleJson = new Gson();

    public String map(Object object) {
        return mGoogleJson.toJson(object);
    }
}
