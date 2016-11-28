package com.lakeel.altla.vision.nearby.presentation.attachment;

import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.service.LocationService;

import android.content.Context;
import android.content.Intent;

public final class BeaconIdAttachmentState implements AttachmentState {

    @Override
    public void startService(Context context, String value) {
        Intent locationServiceIntent = new Intent(context, LocationService.class);
        locationServiceIntent.putExtra(IntentKey.BEACON_ID.name(), value);
        context.startService(locationServiceIntent);
    }
}
