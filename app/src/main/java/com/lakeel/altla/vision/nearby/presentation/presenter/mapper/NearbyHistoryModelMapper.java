package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.domain.model.PassingUserProfile;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyHistoryModel;

public final class NearbyHistoryModelMapper {

    private NearbyHistoryModelMapper() {
    }

    public static NearbyHistoryModel map(PassingUserProfile passingUserProfile) {
        NearbyHistory nearbyHistory = passingUserProfile.nearbyHistory;
        UserProfile userProfile = passingUserProfile.userProfile;

        NearbyHistoryModel model = new NearbyHistoryModel();
        model.historyId = nearbyHistory.historyId;
        model.userId = nearbyHistory.userId;
        model.passingTime = (Long) nearbyHistory.passingTime;
        model.userName = userProfile.name;
        model.imageUri = userProfile.imageUri;
        return model;
    }
}
