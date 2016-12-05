package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public final class ServiceManager {

    private final Class clazz;

    private final Context context;

    public ServiceManager(Context context, Class clazz) {
        this.clazz = clazz;
        this.context = context;
    }

    public void stopService() {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningService : services) {
            if (runningService.service.getClassName().equals(clazz.getName())) {
                Intent intent = new Intent(context, clazz);
                context.stopService(intent);
            }
        }
    }
}
