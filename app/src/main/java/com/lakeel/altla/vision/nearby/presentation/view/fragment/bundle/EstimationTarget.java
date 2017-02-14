package com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class EstimationTarget implements Serializable {

    public final String name;

    public final List<String> beaconIds;

    public EstimationTarget(String name, List<String> beaconIds) {
        this.name = name;
        this.beaconIds = Collections.unmodifiableList(beaconIds);
    }
}
