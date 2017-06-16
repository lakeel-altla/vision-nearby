package com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class EstimationTarget implements Serializable {

    public final String targetName;

    public final List<String> beaconIds;

    public EstimationTarget(@NonNull String targetName, @NonNull List<String> beaconIds) {
        this.targetName = targetName;
        this.beaconIds = Collections.unmodifiableList(beaconIds);
    }
}