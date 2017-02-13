package com.lakeel.altla.vision.nearby.presentation.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import org.altbeacon.beacon.BeaconTransmitter;

import static com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State.ENABLE;
import static com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State.OFF;
import static com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State.SUBSCRIBE_ONLY;
import static org.altbeacon.beacon.BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER;
import static org.altbeacon.beacon.BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS;

public final class BleChecker {

    public enum State {
        OFF("off"), ENABLE("enable"), SUBSCRIBE_ONLY("subscribe_only");

        private String value;

        State(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private Context context;

    private BluetoothManager manager;

    public BleChecker(Context context) {
        this.context = context;
        manager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public State checkState() {

        // BLE of support in Android OS is it from the API 18, advertising support will be from the API 21.
        // Because it may not support the advertisement, check the devices.

        BluetoothAdapter bluetoothAdapter = manager.getAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            return OFF;
        }

        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            // There are also devices that do not support the advertised after Lollipop.
            int result = BeaconTransmitter.checkTransmissionSupported(context);
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
