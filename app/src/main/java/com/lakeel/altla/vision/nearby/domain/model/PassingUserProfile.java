package com.lakeel.altla.vision.nearby.domain.model;

public final class PassingUserProfile {

    public final NearbyHistory nearbyHistory;

    public final UserProfile userProfile;

    public PassingUserProfile(NearbyHistory nearbyHistory, UserProfile userProfile) {
        this.nearbyHistory = nearbyHistory;
        this.userProfile = userProfile;
    }
}