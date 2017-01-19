package com.lakeel.altla.vision.nearby.presentation.presenter.nearby;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.NearbyItemsModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyItemView;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyListView;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class NearbyListPresenter extends BasePresenter<NearbyListView> {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    private static Logger LOGGER = LoggerFactory.getLogger(NearbyListPresenter.class);

    private final Context context;

    private final BluetoothLeScanner scanner;

    private final List<NearbyItemModel> nearbyItemModels = new ArrayList<>();

    private final List<NearbyItemModel> checkedModels = new LinkedList<>();

    private NearbyItemsModelMapper nearbyItemsModelMapper = new NearbyItemsModelMapper();

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

                    Subscription subscription = findBeaconUseCase.execute(beaconId)
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            // Exclude public beacon.
                            .filter(entity -> entity != null)
                            .flatMap(entity -> findUserUseCase.execute(entity.userId).subscribeOn(Schedulers.io()).toObservable())
                            .subscribeOn(Schedulers.io())
                            .map(itemsEntity -> nearbyItemsModelMapper.map(itemsEntity))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(scannedModel -> {
                                for (NearbyItemModel model : nearbyItemModels) {
                                    if (model.userId.equals(scannedModel.userId)) {
                                        return;
                                    }
                                }
                                nearbyItemModels.add(scannedModel);
                                getView().updateItems();
                            }, e -> LOGGER.error("Failed to findList nearby item.", e));
                    subscriptions.add(subscription);
                }
            }
        }
    };

    @Inject
    NearbyListPresenter(Context context) {
        this.context = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        scanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    public void onResume() {
        BleChecker checker = new BleChecker(context);
        BleChecker.State state = checker.checkState();
        if (state == BleChecker.State.OFF) {
            getView().showBleEnabledActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
        if (state == BleChecker.State.ENABLE || state == BleChecker.State.SUBSCRIBE_ONLY) {
            getView().showIndicator();
            subscribe();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        scanner.stopScan(scanCallback);

        getView().hideIndicator();
        getView().drawNormalActionBarColor();
    }

    public void onRefresh() {
        if (isScanning) {
            return;
        }
        subscribe();
    }

    public void onCreateItemView(NearbyItemView nearbyItemView) {
        NearbyItemPresenter nearbyItemPresenter = new NearbyItemPresenter();
        nearbyItemPresenter.onCreateItemView(nearbyItemView);
        nearbyItemView.setItemPresenter(nearbyItemPresenter);
    }

    public int getItemCount() {
        return nearbyItemModels.size();
    }

    public void subscribe() {
        scanner.startScan(scanCallback);

        isScanning = true;

        executor.schedule(() -> {
            // Stop to scan after 10 seconds.
            scanner.stopScan(scanCallback);

            isScanning = false;

            getView().hideIndicator();

            if (nearbyItemModels.size() == 0) {
                getView().showSnackBar(R.string.message_not_found);
            }

            subscriptions.unSubscribe();
        }, 10, TimeUnit.SECONDS);
    }

    public final class NearbyItemPresenter extends BaseItemPresenter<NearbyItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(nearbyItemModels.get(position));
        }

        public void onCheck(NearbyItemModel model) {
            if (model.isChecked) {
                Iterator<NearbyItemModel> iterator = checkedModels.iterator();
                while (iterator.hasNext()) {
                    NearbyItemModel nearbyItemModel = iterator.next();
                    if (nearbyItemModel.userId.equals(model.userId)) {
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
                getView().drawNormalActionBarColor();
            }
        }
    }
}
