package com.lakeel.altla.vision.nearby.presentation.presenter.nearby;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.library.AttachmentListener;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCmJidUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.constants.AttachmentType;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.NearbyItemsModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.subscriber.ForegroundSubscriber;
import com.lakeel.altla.vision.nearby.presentation.subscriber.Subscriber;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyItemView;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class NearbyListPresenter extends BasePresenter<NearbyListView> implements GoogleApiClient.ConnectionCallbacks {

    private class NearbyMessagesListener extends AttachmentListener {

        @Override
        protected void onFound(String type, String value) {
            if (AttachmentType.USER_ID != AttachmentType.toType(type)) {
                return;
            }
            if (MyUser.getUid().equals(value)) {
                return;
            }

            Subscription subscription = findUserUseCase
                    .execute(value)
                    .toObservable()
                    .filter(entity -> entity != null)
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
    }

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindCmJidUseCase findCmJidUseCase;

    @Inject
    SaveCmFavoritesUseCase saveCmFavoritesUseCase;

    private static Logger LOGGER = LoggerFactory.getLogger(NearbyListPresenter.class);

    private final GoogleApiClient googleApiClient;

    private final Subscriber subscriber;

    private final List<NearbyItemModel> nearbyItemModels = new ArrayList<>();

    private final List<NearbyItemModel> checkedModels = new LinkedList<>();

    private NearbyItemsModelMapper nearbyItemsModelMapper = new NearbyItemsModelMapper();

    private CmFavoritesDataMapper cmFavoritesDataMapper = new CmFavoritesDataMapper();

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private boolean isScanning;

    private boolean isCmLinkEnabled;

    private ResolutionResultCallback resultCallback = new ResolutionResultCallback() {
        @Override
        protected void onResolution(Status status) {
            getView().showResolutionSystemDialog(status);
        }
    };

    @Inject
    NearbyListPresenter(Activity activity) {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();
        subscriber = new ForegroundSubscriber(googleApiClient, new NearbyMessagesListener());
    }

    public void onResume() {
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.connect();

        Subscription subscription = findConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bool -> isCmLinkEnabled = bool,
                        e -> LOGGER.error("Failed to find config settings.", e));
        subscriptions.add(subscription);
    }

    public void onPause() {
        googleApiClient.unregisterConnectionCallbacks(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        subscriber.unSubscribe(resultCallback);

        getView().hideIndicator();
        getView().drawNormalActionBarColor();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getView().showIndicator();
        onSubscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void onRefresh() {
        if (isScanning) {
            return;
        }
        onSubscribe();
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

    public void onSubscribe() {
        subscriber.subscribe(resultCallback);

        isScanning = true;

        executor.schedule(() -> {
            // Stop to scanning after 10 seconds.
            subscriber.unSubscribe(resultCallback);

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

    Observable<String> findCmJid(String userId) {
        return findCmJidUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable();
    }

    Observable<Timestamp> saveCmFavorites(CmFavoriteData data) {
        return saveCmFavoritesUseCase.execute(data).subscribeOn(Schedulers.io()).toObservable();
    }
}
