package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;

import rx.Single;

public interface RestCmRepository {

    Single<Timestamp> saveFavorites(CmFavoriteData data);
}
