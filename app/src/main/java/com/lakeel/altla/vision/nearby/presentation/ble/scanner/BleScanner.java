package com.lakeel.altla.vision.nearby.presentation.ble.scanner;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.ble.exception.BleNotSupportedException;

@TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
final class BleScanner implements Scanner {

    private final BluetoothLeScanner bluetoothLeScanner;

    private BleScanCallback bleScanCallback = null;

    private final ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result.getScanRecord() == null) {
                return;
            }
            bleScanCallback.onScanned(result.getRssi(), result.getScanRecord().getBytes());
        }
    };

    BleScanner(@NonNull Context context, @NonNull BleScanCallback scanCallback) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) throw new BleNotSupportedException("This device is not supported for ble. The variable 'bluetoothManager' is null.");

        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) throw new BleNotSupportedException("This device is not supported for ble. The variable 'bluetoothAdapter' is null.");

        this.bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.bleScanCallback = scanCallback;
    }

    @Override
    public void startScan() {
        bluetoothLeScanner.startScan(scanCallback);
    }

    @Override
    public void stopScan() {
        bluetoothLeScanner.stopScan(scanCallback);
    }
}