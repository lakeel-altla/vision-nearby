package com.lakeel.altla.vision.nearby.presentation.ble.scanner;

public interface BleScanCallback {

    void onScan(int rssi, byte[] scanRecord);
}
