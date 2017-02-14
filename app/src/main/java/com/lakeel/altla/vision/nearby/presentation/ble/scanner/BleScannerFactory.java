package com.lakeel.altla.vision.nearby.presentation.ble.scanner;

import android.content.Context;
import android.os.Build;

public final class BleScannerFactory {

    private BleScannerFactory() {
    }

    public static Scanner create(Context context, BleScanCallback scanCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new BleScanner(context, scanCallback);
        } else {
            return new LeScanner(context, scanCallback);
        }
    }
}