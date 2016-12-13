package com.lakeel.altla.vision.nearby.presentation.constants;

public enum AttachmentType {

    UNKNOWN("unknown"), USER_ID("userId"), BEACON_ID("beaconId"), LINE_URL("lineUrl");

    private String value;

    AttachmentType(String value) {
        this.value = value;
    }

    public static AttachmentType toType(String value) {
        for (AttachmentType type : AttachmentType.values()) {
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
