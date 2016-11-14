package com.lakeel.profile.notification.domain.repository;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.profile.notification.domain.model.CmFavoritesData;

import rx.Observable;

public interface RestCmRepository {

    Observable<Timestamp> saveFavorites(CmFavoritesData data);
}
