package com.lakeel.altla.vision.nearby.presentation.attachment;

import com.lakeel.altla.vision.nearby.presentation.constants.AttachmentType;


public final class BeaconAttachmentFactory {

    private BeaconAttachmentFactory() {
    }

    public static com.lakeel.altla.vision.nearby.presentation.attachment.Attachment create(String value) {
        AttachmentType type = AttachmentType.toType(value);
        switch (type) {
            case USER_ID:
                return new UserIdAttachment();
            case LINE_URL:
                return new LineUrlAttachment();
            case BEACON_ID:
                return new BeaconIdAttachment();
            default:
                return null;
        }
    }
}
