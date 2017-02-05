package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;

public final class DeviceTokenMapper {

    public DeviceToken map(String userId, String beaconId, String tokenValue) {
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.userId = userId;
        deviceToken.beaconId = beaconId;
        deviceToken.token = tokenValue;
        return deviceToken;
    }
}