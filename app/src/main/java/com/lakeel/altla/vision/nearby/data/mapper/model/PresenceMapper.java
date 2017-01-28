package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;
import com.lakeel.altla.vision.nearby.domain.model.Presence;

public final class PresenceMapper {

    public Presence map(PresenceEntity entity) {
        Presence presence = new Presence();
        presence.isConnected = entity.isConnected;
        presence.lastOnlineTime = entity.lastOnlineTime;
        return presence;
    }
}
