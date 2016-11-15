package com.lakeel.profile.notification.presentation.attachment;

import com.lakeel.profile.notification.presentation.constants.AttachmentType;


public final class AttachmentStateFactory {

    private AttachmentStateFactory() {
    }

    public static AttachmentState create(String attachmentType) {
        AttachmentType type = AttachmentType.toType(attachmentType);
        switch (type) {
            case USER_ID:
                return new UserIdAttachmentState();
            case LINE_URL:
                return new LineUrlAttachmentState();
            case BEACON_ID:
                return new BeaconIdAttachmentState();
            default:
                return null;
        }
    }
}
