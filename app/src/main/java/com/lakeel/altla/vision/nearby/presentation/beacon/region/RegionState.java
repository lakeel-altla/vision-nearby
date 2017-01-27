package com.lakeel.altla.vision.nearby.presentation.beacon.region;

import com.lakeel.altla.vision.nearby.R;

public enum RegionState {
    ENTER(R.string.region_enter), EXIT(R.string.region_exit);

    private final int value;

    RegionState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
