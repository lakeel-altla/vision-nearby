package com.lakeel.altla.vision.nearby.domain.model;

import android.support.annotation.NonNull;

public final class NearbyHistoryUserProfile {

    public final NearbyHistory nearbyHistory;

    public final UserProfile userProfile;

    public NearbyHistoryUserProfile(@NonNull NearbyHistory nearbyHistory, @NonNull UserProfile userProfile) {
        this.nearbyHistory = nearbyHistory;
        this.userProfile = userProfile;
    }
}