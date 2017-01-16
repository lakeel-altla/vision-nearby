package com.lakeel.altla.vision.nearby.presentation.presenter.estimation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DistanceEstimationView;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class DistanceEstimationPresenter extends BasePresenter<DistanceEstimationView> {

    private final BluetoothAdapter bluetoothAdapter;

    private List<String> beaconIds;

    private final BluetoothAdapter.LeScanCallback scanCallback = (bluetoothDevice, rssi, scanRecord) -> {
        List<ADStructure> structures =
                ADPayloadParser.getInstance().parse(scanRecord);

        for (ADStructure structure : structures) {
            if (structure instanceof EddystoneUID) {
                EddystoneUID eddystoneUID = (EddystoneUID) structure;
                String scannedBeaconId = eddystoneUID.getBeaconIdAsString().toLowerCase();

                Subscription subscription = Observable.from(beaconIds)
                        // Filter beacons.
                        .filter(beaconId -> beaconId.equals(scannedBeaconId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(beaconId -> {
                            // Calculate distance.
                            int txPower = eddystoneUID.getTxPower();
                            double distance = Math.pow(10d, ((double) txPower - rssi) / 20.0) / 100;

                            String meters = String.format(Locale.getDefault(), "%.2f", distance);
                            getView().showDistance(meters);
                        });
                subscriptions.add(subscription);
            }
        }
    };

    @Inject
    DistanceEstimationPresenter(Context context) {
        // TODO: Check Ble
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void onResume() {
        // TODO: Deprecated
        bluetoothAdapter.startLeScan(scanCallback);
    }

    @Override
    public void onStop() {
        super.onStop();

        // TODO: Deprecated
        bluetoothAdapter.stopLeScan(scanCallback);
    }

    public void setBeaconIds(List<String> beaconIds) {
        this.beaconIds = beaconIds;
    }

    public void subscribe() {
        // TODO: Deprecated
        bluetoothAdapter.startLeScan(scanCallback);
    }
}
