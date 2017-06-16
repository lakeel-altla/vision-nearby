package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionType;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    SaveNearbyHistoryUseCase() {
    }

    public Single<String> execute(String passingUserId, RegionType regionType) {
        NearbyHistory nearbyHistory = new NearbyHistory();
        nearbyHistory.userId = passingUserId;
        nearbyHistory.isEntered = RegionType.ENTER == regionType;
        nearbyHistory.passingTime = ServerValue.TIMESTAMP;
        return repository.save(CurrentUser.getUid(), nearbyHistory).subscribeOn(Schedulers.io());
    }
}
