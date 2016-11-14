package com.lakeel.profile.notification.presentation.attachment;

import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.service.LINEService;

import android.content.Context;
import android.content.Intent;

public class LineUrlAttachment implements AttachmentState {

    @Override
    public void startService(Context context, String value) {
        Intent lineIntent = new Intent(context, LINEService.class);
        lineIntent.putExtra(IntentKey.LINE_URL.name(), value);
        context.startService(lineIntent);
    }
}
