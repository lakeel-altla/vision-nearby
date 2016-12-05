package com.lakeel.altla.vision.nearby.presentation.constants;

public enum BeaconAttachment {

    UNKNOWN("unknown"), USER_ID("userId"), BEACON_ID("beaconId"), LINE_URL("lineUrl");

    private String value;

    BeaconAttachment(String value) {
        this.value = value;
    }

    public static BeaconAttachment toType(String value) {
        for (BeaconAttachment type : BeaconAttachment.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getValue() {
        return value;
    }
}
