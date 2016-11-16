package com.lakeel.profile.notification.presentation.attachment;

import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.service.RecentlyService;

import android.content.Context;
import android.content.Intent;

public class UserIdAttachmentState implements AttachmentState {

    @Override
    public void startService(Context context, String value) {

        // Note:
        // Check user authentication, because this method may be performed at the time of signing out.
        if (!MyUser.isAuthenticated()) {
            return;
        }
        if (MyUser.getUid().equals(value)) {
            return;
        }

        Intent recentlyServiceIntent = new Intent(context, RecentlyService.class);
        recentlyServiceIntent.putExtra(IntentKey.USER_ID.name(), value);
        context.startService(recentlyServiceIntent);
    }
}
