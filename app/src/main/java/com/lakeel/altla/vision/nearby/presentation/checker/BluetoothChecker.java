package com.lakeel.altla.vision.nearby.presentation.checker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import org.altbeacon.beacon.BeaconTransmitter;

import static com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState.DISABLE;
import static com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState.ENABLE;
import static com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState.OFF;
import static com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState.SUBSCRIBE_ONLY;
import static org.altbeacon.beacon.BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER;
import static org.altbeacon.beacon.BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS;

public final class BluetoothChecker {

    public enum BleState {
        OFF, DISABLE, ENABLE, SUBSCRIBE_ONLY
    }

    private Context mContext;

    private BluetoothManager mManager;

    public BluetoothChecker(Context context) {
        mContext = context;
        mManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public BleState getState() {

        // BLE of support in Android OS is it from the API 18, advertising support will be from the API 21.
        // Because it may not support the advertisement, check the devices.

        BluetoothAdapter bluetoothAdapter = mManager.getAdapter();

        if (bluetoothAdapter == null) {
            return DISABLE;
        }

        if (!bluetoothAdapter.isEnabled()) {
            return OFF;
        }

        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            // There are also devices that do not support the advertised after Lollipop.
            int result = BeaconTransmitter.checkTransmissionSupported(mContext);
            if ((result == NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS) || (result == NOT_SUPPORTED_CANNOT_GET_ADVERTISER)) {
                return SUBSCRIBE_ONLY;
            }
        } else {
            // If later Android version of Ice Cream Sandwich(4.0), it can't advertise packet.
            return SUBSCRIBE_ONLY;
        }

        return ENABLE;
    }
}
