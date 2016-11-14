package com.lakeel.altla.library;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.support.annotation.NonNull;

public abstract class ResolutionResultCallback implements ResultCallback<Status> {

    @Override
    public final void onResult(@NonNull Status status) {
        if (!status.isSuccess() && status.hasResolution()) {
            onResolution(status);
        }
    }

    protected abstract void onResolution(Status status);
}
