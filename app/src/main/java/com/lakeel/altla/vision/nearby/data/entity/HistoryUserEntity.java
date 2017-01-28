package com.lakeel.altla.vision.nearby.data.entity;

public final class HistoryUserEntity {

    public final HistoryEntity historyEntity;

    public final UserEntity userEntity;

    public HistoryUserEntity(HistoryEntity historyEntity, UserEntity userEntity) {
        this.historyEntity = historyEntity;
        this.userEntity = userEntity;
    }
}
