package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllNearbyUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.checker.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.scanner.BleScanCallback;
import com.lakeel.altla.vision.nearby.presentation.ble.scanner.Scanner;
import com.lakeel.altla.vision.nearby.presentation.ble.scanner.BleScannerFactory;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.NearbyUsersModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserItemView;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserListView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class NearbyUserListPresenter extends BasePresenter<NearbyUserListView> {

    @Inject
    FindAllNearbyUserUseCase findAllNearbyUserUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyUserListPresenter.class);

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private final List<NearbyUserModel> viewModels = new ArrayList<>();

    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private final Context context;

    private final Scanner scanner;

    private boolean isScanning;

    @SuppressWarnings("FieldCanBeLocal")
    private final BleScanCallback scanCallback = new BleScanCallback() {

        @Override
        public void onScanned(int rssi, byte[] scanRecord) {
            List<ADStructure> structures =
                    ADPayloadParser.getInstance().parse(scanRecord);

            for (ADStructure structure : structures) {
                if (structure instanceof EddystoneUID) {
                    EddystoneUID eddystoneUID = (EddystoneUID) structure;
                    String beaconId = eddystoneUID.getBeaconIdAsString().toLowerCase();

                    Subscription subscription = findAllNearbyUserUseCase.execute(beaconId)
                            .map(NearbyUsersModelMapper::map)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(scannedModel -> {
                                for (NearbyUserModel model : viewModels) {
                                    // Check already scanned.
                                    if (model.userId.equals(scannedModel.userId)) {
                                        return;
                                    }
                                }

                                viewModels.add(scannedModel);
                                getView().updateItems();
                            }, e -> {
                                LOGGER.error("Failed.", e);
                            });
                    subscriptions.add(subscription);
                }
            }
        }
    };

    @Inject
    NearbyUserListPresenter(Context context) {
        this.context = context;
        this.scanner = BleScannerFactory.create(context, scanCallback);
    }

    public void onActivityCreated() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAccessFineLocationPermission();
        } else {
            if (isBleEnabled()) {
                getView().showIndicator();
                subscribe();
            } else {
                getView().showBleEnabledActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        }
    }

    public void onStop() {
        subscriptions.unSubscribe();

        getView().hideIndicator();

        scanner.stopScan();
    }

    public void onBleEnabled() {
        getView().showIndicator();
        subscribe();
    }

    public void onRefresh() {
        if (isScanning) {
            return;
        }

        viewModels.clear();
        getView().updateItems();

        subscribe();
    }

    public void onCreateItemView(NearbyUserItemView nearbyUserItemView) {
        NearbyUserItemPresenter nearbyUserItemPresenter = new NearbyUserItemPresenter();
        nearbyUserItemPresenter.onCreateItemView(nearbyUserItemView);
        nearbyUserItemView.setItemPresenter(nearbyUserItemPresenter);
    }

    public int getItemCount() {
        return viewModels.size();
    }

    public void onAccessFineLocationGranted() {
        if (isBleEnabled()) {
            getView().showIndicator();
            subscribe();
        } else {
            getView().showBleEnabledActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }

    private void checkAccessFineLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                if (isBleEnabled()) {
                    getView().showIndicator();
                    subscribe();
                } else {
                    getView().showBleEnabledActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                }
            } else {
                getView().requestAccessFineLocationPermission();
            }
        }
    }

    private boolean isBleEnabled() {
        BleChecker checker = new BleChecker(context);
        return checker.checkState() != BleChecker.State.OFF;
    }

    private void subscribe() {
        scanner.startScan();

        isScanning = true;

        executor.schedule(() -> {
            // Stop token scan after 5 seconds.
            scanner.stopScan();

            isScanning = false;

            getView().hideIndicator();

            if (viewModels.size() == 0) {
                getView().showSnackBar(R.string.snackBar_message_not_found);
            }

            subscriptions.unSubscribe();
        }, 5, TimeUnit.SECONDS);
    }

    public final class NearbyUserItemPresenter extends BaseItemPresenter<NearbyUserItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(viewModels.get(position));
        }
    }
}