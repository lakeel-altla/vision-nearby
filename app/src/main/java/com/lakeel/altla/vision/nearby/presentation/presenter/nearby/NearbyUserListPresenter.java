package com.lakeel.altla.vision.nearby.presentation.presenter.nearby;

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
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindNearbyUsersUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.NearbyUsersModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserItemView;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserListView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class NearbyUserListPresenter extends BasePresenter<NearbyUserListView> {

    @Inject
    FindNearbyUsersUseCase findNearbyUsersUseCase;

    private final Context context;

    private final BluetoothLeScanner scanner;

    private final List<NearbyUserModel> nearbyUserModels = new ArrayList<>();

    private final List<NearbyUserModel> checkedModels = new LinkedList<>();

    private NearbyUsersModelMapper modelMapper = new NearbyUsersModelMapper();

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private boolean isScanning;

    private ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord scanRecord = result.getScanRecord();
            if (scanRecord == null) {
                return;
            }

            List<ADStructure> structures =
                    ADPayloadParser.getInstance().parse(scanRecord.getBytes());

            for (ADStructure structure : structures) {
                if (structure instanceof EddystoneUID) {
                    EddystoneUID eddystoneUID = (EddystoneUID) structure;
                    String beaconId = eddystoneUID.getBeaconIdAsString().toLowerCase();

                    Subscription subscription = findNearbyUsersUseCase.execute(beaconId)
                            .map(user -> modelMapper.map(user))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(scannedModel -> {
                                for (NearbyUserModel model : nearbyUserModels) {
                                    // Check already scanned.
                                    if (model.userId.equals(scannedModel.userId)) {
                                        return;
                                    }
                                }

                                nearbyUserModels.add(scannedModel);
                                getView().updateItems();
                            }, new ErrorAction<>());
                    subscriptions.add(subscription);
                }
            }
        }
    };

    @Inject
    NearbyUserListPresenter(Context context) {
        this.context = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        scanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    public void onActivityCreated() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAccessFineLocationPermission();
        } else {
            checkDeviceBle();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        getView().hideIndicator();
        getView().drawDefaultActionBarColor();

        scanner.stopScan(scanCallback);
    }

    public void onRefresh() {
        if (isScanning) {
            return;
        }
        subscribe();
    }

    public void onCreateItemView(NearbyUserItemView nearbyUserItemView) {
        NearbyUserItemPresenter nearbyUserItemPresenter = new NearbyUserItemPresenter();
        nearbyUserItemPresenter.onCreateItemView(nearbyUserItemView);
        nearbyUserItemView.setItemPresenter(nearbyUserItemPresenter);
    }

    public int getItemCount() {
        return nearbyUserModels.size();
    }

    public void onBleEnabled() {
        subscribe();
    }

    public void onAccessFineLocationGranted() {
        checkDeviceBle();
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
            getView().showIndicator();
            subscribe();
        }
    }

    private void subscribe() {
        scanner.startScan(scanCallback);

        isScanning = true;

        executor.schedule(() -> {
            // Stop to scan after 2 seconds.
            scanner.stopScan(scanCallback);

            isScanning = false;

            getView().hideIndicator();

            if (nearbyUserModels.size() == 0) {
                getView().showSnackBar(R.string.message_not_found);
            }

            subscriptions.unSubscribe();
        }, 2, TimeUnit.SECONDS);
    }

    public final class NearbyUserItemPresenter extends BaseItemPresenter<NearbyUserItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(nearbyUserModels.get(position));
        }

        public void onCheck(NearbyUserModel model) {
            if (model.isChecked) {
                Iterator<NearbyUserModel> iterator = checkedModels.iterator();
                while (iterator.hasNext()) {
                    NearbyUserModel nearbyUserModel = iterator.next();
                    if (nearbyUserModel.userId.equals(model.userId)) {
                        iterator.remove();
                    }
                }
            } else {
                checkedModels.add(model);
            }

            model.isChecked = !model.isChecked;

            if (0 < checkedModels.size()) {
                getView().drawEditableActionBarColor();
            } else {
                getView().hideOptionMenu();
                getView().drawDefaultActionBarColor();
            }
        }
    }
}
