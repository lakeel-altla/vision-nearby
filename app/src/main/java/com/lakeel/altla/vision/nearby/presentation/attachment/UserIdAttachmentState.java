package com.lakeel.altla.vision.nearby.presentation.attachment;

import android.content.Context;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.service.HistoryService;

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

        Intent historyServiceIntent = new Intent(context, HistoryService.class);
        historyServiceIntent.putExtra(IntentKey.USER_ID.name(), value);
        context.startService(historyServiceIntent);
    }
}
