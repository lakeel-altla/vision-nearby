package com.lakeel.altla.library;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.support.annotation.NonNull;

/**
 * An abstract class for receiving a {@link Result} from a {@link PendingResult} as an asynchronous
 * callback.
 */
public abstract class ResolutionResultCallback implements ResultCallback<Status> {

    /**
     * ${inheritDoc}
     */
    @Override
    public final void onResult(@NonNull Status status) {
        if (!status.isSuccess() && status.hasResolution()) {
            onResolution(status);
        }
    }

    /**
     * Called when can resolution.
     *
     * @param status The results of work.
     */
    protected abstract void onResolution(Status status);
}
