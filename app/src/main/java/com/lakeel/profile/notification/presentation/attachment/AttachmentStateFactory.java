package com.lakeel.profile.notification.presentation.attachment;

import com.lakeel.profile.notification.presentation.constants.AttachmentType;


public final class AttachmentStateFactory {

    private AttachmentStateFactory() {
    }

    public static AttachmentState create(String value) {
        AttachmentType type = AttachmentType.toType(value);
        switch (type) {
            case USER_ID:
                return new UserIdAttachment();
            case LINE_URL:
                return new LineUrlAttachment();
            default:
                return null;
        }
    }
}
