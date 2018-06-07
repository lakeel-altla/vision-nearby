package com.lakeel.altla.vision.nearby.presentation.ble.exception;

import android.support.annotation.NonNull;

public final class BleNotSupportedException extends RuntimeException {

    public BleNotSupportedException(@NonNull String message) {
        super(message);
    }
}
