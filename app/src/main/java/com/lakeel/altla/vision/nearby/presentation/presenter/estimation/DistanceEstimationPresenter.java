package com.lakeel.altla.vision.nearby.presentation.presenter.estimation;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.lakeel.altla.vision.nearby.presentation.beacon.distance.Distance;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DistanceEstimationView;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class DistanceEstimationPresenter extends BasePresenter<DistanceEstimationView> {

    private final Context context;

    private final BluetoothLeScanner scanner;

    private List<String> beaconIds;

    private ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            ScanRecord scanRecord = result.getScanRecord();
            if (scanRecord == null) {
                return;
            }

            List<ADStructure> structures =
                    ADPayloadParser.getInstance().parse(scanRecord.getBytes());

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
                                Distance distance = new Distance(eddystoneUID.getTxPower(), result.getRssi());
                                getView().showDistance(distance.getMeters());
                            });
                    subscriptions.add(subscription);
                }
            }
        }
    };

    @Inject
    DistanceEstimationPresenter(Context context) {
        this.context = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        scanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public void onStop() {
        super.onStop();
        scanner.stopScan(scanCallback);
    }

    public void setBeaconIds(List<String> beaconIds) {
        this.beaconIds = beaconIds;
    }

    public void onActivityCreated() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAccessFineLocationPermission();
        } else {
            checkDeviceBle();
        }
    }

    public void onAccessFineLocationGranted() {
        checkDeviceBle();
    }

    public void subscribe() {
        getView().startAnimation();
        scanner.startScan(scanCallback);
    }

    private void checkAccessFineLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                checkDeviceBle();
            } else {
                getView().requestAccessFineLocationPermission();
            }
        }
    }

    private void checkDeviceBle() {
        BleChecker checker = new BleChecker(context);
        BleChecker.State state = checker.checkState();
        if (state == BleChecker.State.OFF) {
            getView().showBleEnabledActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        } else if (state == BleChecker.State.ENABLE || state == BleChecker.State.SUBSCRIBE_ONLY) {
            subscribe();
        }
    }
}
