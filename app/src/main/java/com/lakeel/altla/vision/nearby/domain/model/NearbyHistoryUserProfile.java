package com.lakeel.altla.vision.nearby.domain.model;

public final class NearbyHistoryUserProfile {

    public final NearbyHistory nearbyHistory;

    public final UserProfile userProfile;

    public NearbyHistoryUserProfile(NearbyHistory nearbyHistory, UserProfile userProfile) {
        this.nearbyHistory = nearbyHistory;
        this.userProfile = userProfile;
    }
}