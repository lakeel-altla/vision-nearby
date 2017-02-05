package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.data.entity.ConnectionEntity;
import com.lakeel.altla.vision.nearby.domain.model.Connection;

public final class ConnectionMapper {

    public Connection map(DataSnapshot snapshot) {
        ConnectionEntity entity = snapshot.getValue(ConnectionEntity.class);

        Connection connection = new Connection();
        connection.isConnected = entity.isConnected;
        connection.lastOnlineTime = entity.lastOnlineTime;

        return connection;
    }
}
