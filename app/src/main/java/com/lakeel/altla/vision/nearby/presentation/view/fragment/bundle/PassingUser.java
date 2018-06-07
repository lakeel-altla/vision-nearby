package com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle;

import java.io.Serializable;

public final class PassingUser implements Serializable {

    public final String historyId;

    public final String userId;

    public final String userName;

    public PassingUser(String historyId, String userId, String userName) {
        this.historyId = historyId;
        this.userId = userId;
        this.userName = userName;
    }
}