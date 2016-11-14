package com.lakeel.altla.library;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public abstract class AttachmentListener extends MessageListener {

    @Override
    public final void onFound(Message message) {
        String type = message.getType();
        String value = new String(message.getContent());
        onFound(type, value);
    }

    protected abstract void onFound(String type, String value);
}
