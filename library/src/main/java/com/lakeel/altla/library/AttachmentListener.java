package com.lakeel.altla.library;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

/**
 * A abstract class for receiving subscribed messages. This callback will be delivered when messages are found.
 */
public abstract class AttachmentListener extends MessageListener {

    /**
     * ${inheritDoc}
     */
    @Override
    public final void onFound(Message message) {
        String type = message.getType();
        String value = new String(message.getContent());
        onFound(type, value);
    }

    /**
     * Called with attachments data.
     *
     * @param type The attachment of type.
     * @param value The attachment of value.
     */
    protected abstract void onFound(String type, String value);
}
