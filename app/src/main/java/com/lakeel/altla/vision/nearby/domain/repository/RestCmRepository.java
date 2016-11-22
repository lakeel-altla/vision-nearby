package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;

import rx.Observable;

public interface RestCmRepository {

    Observable<Timestamp> saveFavorites(CmFavoritesData data);
}
