package com.lakeel.profile.notification.presentation.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public final class ServiceManager {

    private final Class mClass;

    private final Context mContext;

    public ServiceManager(Context context, Class clazz) {
        mClass = clazz;
        mContext = context;
    }

    public void stopService() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningService : services) {
            if (runningService.service.getClassName().equals(mClass.getName())) {
                Intent intent = new Intent(mContext, mClass);
                mContext.stopService(intent);
            }
        }
    }
}
