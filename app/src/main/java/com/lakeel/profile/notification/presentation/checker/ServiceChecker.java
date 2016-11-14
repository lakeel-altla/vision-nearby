package com.lakeel.profile.notification.presentation.checker;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public final class ServiceChecker {

    private Context mContext;

    private String mServiceName;

    public ServiceChecker(Context context, Class clazz) {
        mContext = context;
        mServiceName = clazz.getName();
    }

    public boolean isRunnnig() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            if (serviceInfo.service.getClassName().equals(mServiceName)) {
                return true;
            }
        }
        return false;
    }

}
