package com.lakeel.altla.vision.nearby.presentation.beacon.region;

public enum RegionState {
    EXIT(0), ENTER(1);

    private final int value;

    RegionState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RegionState toRegionState(int value) {
        for (RegionState state : RegionState.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        return RegionState.EXIT;
    }
}
