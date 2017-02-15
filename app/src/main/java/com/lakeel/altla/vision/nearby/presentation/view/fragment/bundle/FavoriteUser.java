package com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle;


import java.io.Serializable;

public final class FavoriteUser implements Serializable {

    public final String userId;

    public final String name;

    public FavoriteUser(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
