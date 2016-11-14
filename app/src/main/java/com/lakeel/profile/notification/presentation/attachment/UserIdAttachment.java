package com.lakeel.profile.notification.presentation.attachment;

import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.service.LocationService;
import com.lakeel.profile.notification.presentation.service.RecentlyService;

import android.content.Context;
import android.content.Intent;

public class UserIdAttachment implements AttachmentState {

    @Override
    public void startService(Context context, String value) {
        if (MyUser.getUid().equals(value)) {
            return;
        }

        Intent recentlyServiceIntent = new Intent(context, RecentlyService.class);
        recentlyServiceIntent.putExtra(IntentKey.USER_ID.name(), value);
        context.startService(recentlyServiceIntent);

        Intent locationServiceIntent = new Intent(context, LocationService.class);
        locationServiceIntent.putExtra(IntentKey.USER_ID.name(), value);
        context.startService(locationServiceIntent);
    }
}
