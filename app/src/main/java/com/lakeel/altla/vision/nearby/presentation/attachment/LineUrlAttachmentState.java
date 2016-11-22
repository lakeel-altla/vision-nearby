package com.lakeel.altla.vision.nearby.presentation.attachment;

import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.service.LINEService;

import android.content.Context;
import android.content.Intent;

public class LineUrlAttachmentState implements AttachmentState {

    @Override
    public void startService(Context context, String value) {
        Intent lineIntent = new Intent(context, LINEService.class);
        lineIntent.putExtra(IntentKey.LINE_URL.name(), value);
        context.startService(lineIntent);
    }
}
