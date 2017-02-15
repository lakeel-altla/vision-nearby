package com.lakeel.altla.vision.nearby.presentation.ble.scanner;

public interface BleScanCallback {

    void onScanned(int rssi, byte[] scanRecord);
}
