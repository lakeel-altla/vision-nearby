package com.lakeel.altla.vision.nearby.presentation.presenter.nearby;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.support.annotation.IntRange;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.altBeacon.BeaconRangeNotifier;
import com.lakeel.altla.vision.nearby.altBeacon.ForegroundBeaconManager;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCmJidUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.NearbyItemsModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyItemView;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyListView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class NearbyListPresenter extends BasePresenter<NearbyListView> implements BeaconConsumer {

    private BeaconRangeNotifier notifier = new BeaconRangeNotifier() {

        @Override
        protected void onEddystoneUidFound(String beaconId) {
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
                    }, e -> LOGGER.error("Failed to find nearby item.", e));
            subscriptions.add(subscription);
        }
    };

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindCmJidUseCase findCmJidUseCase;

    @Inject
    SaveCmFavoritesUseCase saveCmFavoritesUseCase;

    private static Logger LOGGER = LoggerFactory.getLogger(NearbyListPresenter.class);

    private final List<NearbyItemModel> nearbyItemModels = new ArrayList<>();

    private final List<NearbyItemModel> checkedModels = new LinkedList<>();

    private NearbyItemsModelMapper nearbyItemsModelMapper = new NearbyItemsModelMapper();

    private CmFavoritesDataMapper cmFavoritesDataMapper = new CmFavoritesDataMapper();

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private boolean isScanning;

    private boolean isCmLinkEnabled;

    private Context context;

    private ForegroundBeaconManager beaconManager;

    private Region region;

    @Inject
    NearbyListPresenter(Context context) {
        this.context = context;

        beaconManager = ForegroundBeaconManager.getInstance(context);
//        beaconManager = BeaconManager.getInstanceForApplication(context);

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));

        beaconManager.addRangeNotifier(notifier);

        region = new Region(UUID.randomUUID().toString(), null, null, null);
    }

    public void onResume() {
        beaconManager.bind(this);

        Subscription subscription = findConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bool -> isCmLinkEnabled = bool,
                        e -> LOGGER.error("Failed to find CM configuration.", e));
        subscriptions.add(subscription);
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            LOGGER.error("Failed to unSubscribe.");
        }

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

    public void onShareSelected() {
        getView().showShareSheet();
    }

    public void onAddToCmFavorite() {
        List<String> nearbyIds = new ArrayList<>(checkedModels.size());

        for (NearbyItemModel model : checkedModels) {
            nearbyIds.add(model.userId);
        }

        Subscription subscription = Observable
                .from(nearbyIds)
                .flatMap(this::findCmJid)
                .toList()
                .map(userIds -> cmFavoritesDataMapper.map(userIds))
                .flatMap(this::saveCmFavorites)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timestamp -> {
                    for (NearbyItemModel model : checkedModels) {
                        model.isChecked = false;
                    }
                    checkedModels.clear();

                    getView().hideOptionMenu();
                    getView().drawNormalActionBarColor();
                    getView().updateItems();
                    getView().showSnackBar(R.string.message_added);
                }, e -> {
                    LOGGER.error("Failed to add nearby users to COMPANY Messenger.", e);
                    getView().showSnackBar(R.string.error_not_added);
                });

        subscriptions.add(subscription);
    }

    public void subscribe() {
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            LOGGER.error("Failed to subscribe.");
        }

        isScanning = true;

        executor.schedule(() -> {
            // Stop to scanning after 10 seconds.
            try {
                beaconManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                LOGGER.error("Failed to unSubscribe.");
            }

            isScanning = false;

            getView().hideIndicator();

            if (nearbyItemModels.size() == 0) {
                getView().showSnackBar(R.string.message_not_found);
            }

            subscriptions.unSubscribe();
        }, 10, TimeUnit.SECONDS);
    }

    public boolean isCmLinkEnabled() {
        return isCmLinkEnabled;
    }

    @Override
    public void onBeaconServiceConnect() {
        getView().showIndicator();
        subscribe();
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent, serviceConnection, i);
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
                if (isCmLinkEnabled) {
                    getView().showOptionMenu();
                }
                getView().drawEditableActionBarColor();
            } else {
                getView().hideOptionMenu();
                getView().drawNormalActionBarColor();
            }
        }
    }

    private Observable<String> findCmJid(String userId) {
        return findCmJidUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable();
    }

    private Observable<Timestamp> saveCmFavorites(CmFavoriteData data) {
        return saveCmFavoritesUseCase.execute(data).subscribeOn(Schedulers.io()).toObservable();
    }
}
