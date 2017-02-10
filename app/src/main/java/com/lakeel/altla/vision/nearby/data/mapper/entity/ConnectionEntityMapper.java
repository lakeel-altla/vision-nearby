package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.entity.ConnectionEntity;

public final class ConnectionEntityMapper {

    public ConnectionEntity map() {
        ConnectionEntity entity = new ConnectionEntity();
        entity.isConnected = true;
        entity.lastOnlineTime = ServerValue.TIMESTAMP;
        return entity;
    }
}
