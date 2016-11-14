package com.lakeel.profile.notification.presentation.constants;

import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.service.LINEService;
import com.lakeel.profile.notification.presentation.service.LocationService;
import com.lakeel.profile.notification.presentation.service.RecentlyService;

import android.content.Context;
import android.content.Intent;

public enum AttachmentType {

    UNKNOWN("unknown") {
        @Override
        public void startService(Context context, String value) {
            throw new UnsupportedOperationException();
        }
    },
    USER_ID("userId") {
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
    },
    LINE_URL("lineUrl") {
        @Override
        public void startService(Context context, String value) {
            Intent lineIntent = new Intent(context, LINEService.class);
            lineIntent.putExtra(IntentKey.LINE_URL.name(), value);
            context.startService(lineIntent);
        }
    };

    private String mValue;

    AttachmentType(String value) {
        mValue = value;
    }

    public static AttachmentType toType(String value) {
        for (AttachmentType type : AttachmentType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getValue() {
        return mValue;
    }

    public abstract void startService(Context context, String value);
}
