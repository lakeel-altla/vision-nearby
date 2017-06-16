package com.lakeel.altla.vision.nearby.data.execption;

import android.support.annotation.NonNull;

public final class DataStoreException extends RuntimeException {

    public DataStoreException(@NonNull Throwable cause) {
        super(cause);
    }

    public DataStoreException(@NonNull String message) {
        super(message);
    }
}
