package com.lakeel.altla.vision.nearby.presentation.ble.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

final class LeScanner implements Scanner {

    private final BluetoothAdapter bluetoothAdapter;

    private BleScanCallback bleScanCallback = null;

    private BluetoothAdapter.LeScanCallback scanCallback = (bluetoothDevice, rssi, scanRecord) -> {
        if (scanRecord == null) {
            return;
        }

        bleScanCallback.onScan(rssi, scanRecord);
    };

    LeScanner(Context context, BleScanCallback bleScanCallback) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();
        this.bleScanCallback = bleScanCallback;
    }

    @Override
    public void startScan() {
        bluetoothAdapter.startLeScan(scanCallback);
    }

    @Override
    public void stopScan() {
        bluetoothAdapter.stopLeScan(scanCallback);
    }
}
