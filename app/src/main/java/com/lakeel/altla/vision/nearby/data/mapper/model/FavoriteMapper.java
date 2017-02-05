package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;

public final class FavoriteMapper {

    public Favorite map(DataSnapshot snapshot) {
        Favorite favorite = new Favorite();
        favorite.userId = snapshot.getKey();
        return favorite;
    }
}