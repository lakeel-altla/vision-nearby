package com.lakeel.profile.notification.presentation.constants;

public enum AttachmentType {

    UNKNOWN("unknown"), USER_ID("userId"), BEACON_ID("beaconId"), LINE_URL("lineUrl");

    private String mValue;

    AttachmentType(String value) {
        mValue = value;
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
        return mValue;
    }
}
