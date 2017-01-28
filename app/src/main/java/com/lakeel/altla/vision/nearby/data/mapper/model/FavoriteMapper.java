package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.domain.model.Favorite;

public final class FavoriteMapper {

    public Favorite map(String userId) {
        Favorite favorite = new Favorite();
        favorite.userId = userId;
        return favorite;
    }
}
