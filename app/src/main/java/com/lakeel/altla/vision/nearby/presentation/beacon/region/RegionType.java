package com.lakeel.altla.vision.nearby.presentation.beacon.region;

public enum RegionType {
    EXIT(0), ENTER(1);

    private final int value;

    RegionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RegionType toRegionType(int value) {
        for (RegionType type : RegionType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }

        return RegionType.EXIT;
    }
}
