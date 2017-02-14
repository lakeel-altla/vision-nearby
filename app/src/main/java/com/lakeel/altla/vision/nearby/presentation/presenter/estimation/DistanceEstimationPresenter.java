package com.lakeel.altla.vision.nearby.presentation.presenter.estimation;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.beacon.distance.Distance;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.scanner.BleScanCallback;
import com.lakeel.altla.vision.nearby.presentation.ble.scanner.Scanner;
import com.lakeel.altla.vision.nearby.presentation.ble.scanner.BleScannerFactory;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DistanceEstimationView;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;
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

    private static final String BUNDLE_ESTIMATION_TARGET = "estimationTarget";

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private final Context context;

    private final Scanner scanner;

    private EstimationTarget target;

    @SuppressWarnings("FieldCanBeLocal")
    private final BleScanCallback scanCallback = new BleScanCallback() {

        @Override
        public void onScan(int rssi, byte[] scanRecord) {
            List<ADStructure> structures =
                    ADPayloadParser.getInstance().parse(scanRecord);

            for (ADStructure structure : structures) {
                if (structure instanceof EddystoneUID) {
                    EddystoneUID eddystoneUID = (EddystoneUID) structure;
                    String scannedBeaconId = eddystoneUID.getBeaconIdAsString().toLowerCase();

                    Subscription subscription = Observable.from(target.beaconIds)
                            // Filter beacons.
                            .filter(beaconId -> beaconId.equals(scannedBeaconId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(beaconId -> {
                                // Calculate distance.
                                Distance distance = new Distance(eddystoneUID.getTxPower(), rssi);
                                String distanceMessage = context.getResources().getString(R.string.snackBar_message_device_distance_format, distance.getMeters());
                                getView().showDistanceMessage(distanceMessage);
                            });

                    subscriptions.add(subscription);
                }
            }
        }
    };

    @Inject
    DistanceEstimationPresenter(Context context) {
        this.context = context;
        this.scanner = BleScannerFactory.create(context, scanCallback);
    }

    public void onCreateView(DistanceEstimationView view, Bundle bundle) {
        super.onCreateView(view);
        this.target = (EstimationTarget) bundle.getSerializable(BUNDLE_ESTIMATION_TARGET);
    }

    public void onActivityCreated() {
        getView().showTitle(target.name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAccessFineLocationPermission();
        } else {
            checkDeviceBle();
        }
    }

    public void onStop() {
        subscriptions.unSubscribe();
        scanner.stopScan();
    }

    public void onAccessFineLocationGranted() {
        checkDeviceBle();
    }

    public void subscribe() {
        getView().startAnimation();
        scanner.startScan();
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
