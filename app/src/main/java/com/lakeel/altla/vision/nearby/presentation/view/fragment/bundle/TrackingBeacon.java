package com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class TrackingBeacon implements Serializable {

    public final String id;

    public final String name;

    public TrackingBeacon(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
