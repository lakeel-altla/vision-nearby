package com.lakeel.altla.vision.nearby.presentation.attachment;

import com.lakeel.altla.vision.nearby.presentation.constants.AttachmentType;


public final class BeaconAttachmentFactory {

    private BeaconAttachmentFactory() {
    }

    public static BeaconAttachment create(String attachmentType) {
        AttachmentType type = AttachmentType.toType(attachmentType);
        switch (type) {
            case USER_ID:
                return new UserIdBeaconAttachment();
            case LINE_URL:
                return new LineUrlBeaconAttachment();
            case BEACON_ID:
                return new BeaconIdBeaconAttachment();
            default:
                return null;
        }
    }
}
