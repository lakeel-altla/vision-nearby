package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public final class RunningServiceManager {

    private final Class clazz;

    private final Context context;

    public RunningServiceManager(Context context, Class clazz) {
        this.context = context;
        this.clazz = clazz;
    }

    public void stopService() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().equals(clazz.getName())) {
                Intent intent = new Intent(context, clazz);
                context.stopService(intent);
            }
        }
    }
}
