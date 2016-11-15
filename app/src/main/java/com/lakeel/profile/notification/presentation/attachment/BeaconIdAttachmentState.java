package com.lakeel.profile.notification.presentation.attachment;

import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.service.LocationService;

import android.content.Context;
import android.content.Intent;

public final class BeaconIdAttachmentState implements AttachmentState {

    @Override
    public void startService(Context context, String value) {
        Intent locationServiceIntent = new Intent(context, LocationService.class);
        locationServiceIntent.putExtra(IntentKey.BECON_ID.name(), value);
        context.startService(locationServiceIntent);
    }
}
