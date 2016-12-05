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
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCMJidUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCMFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.constants.BeaconAttachment;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class NearbyListPresenter extends BasePresenter<NearbyListView> implements GoogleApiClient.ConnectionCallbacks {

    private class NearbyMessagesListener extends AttachmentListener {

        @Override
        protected void onFound(String type, String value) {
            if (BeaconAttachment.USER_ID != BeaconAttachment.toType(type)) {
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
                    .map(itemsEntity -> mMapper.map(itemsEntity))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(scannedModel -> {
                        for (NearbyItemModel model : mNearbyItemModels) {
                            if (model.mId.equals(scannedModel.mId)) {
                                return;
                            }
                        }
                        mNearbyItemModels.add(scannedModel);
                        getView().updateItems();
                    }, e -> LOGGER.error("Failed to find nearby item.", e));

            reusableCompositeSubscription.add(subscription);
        }
    }

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindCMJidUseCase findCMJidUseCase;

    @Inject
    SaveCMFavoritesUseCase saveCMFavoritesUseCase;

    private static Logger LOGGER = LoggerFactory.getLogger(NearbyListPresenter.class);

    private final GoogleApiClient mGoogleApiClient;

    private final Subscriber mSubscriber;

    private final List<NearbyItemModel> mNearbyItemModels = new ArrayList<>();

    private final List<NearbyItemModel> mCheckedModels = new LinkedList<>();

    private NearbyItemsModelMapper mMapper = new NearbyItemsModelMapper();

    private CmFavoritesDataMapper cmFavoritesDataMapper = new CmFavoritesDataMapper();

    private ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);

    private boolean mScanning;

    private boolean mCmLinkEnabled;

    private ResolutionResultCallback mResultCallback = new ResolutionResultCallback() {
        @Override
        protected void onResolution(Status status) {
            getView().showResolutionSystemDialog(status);
        }
    };

    @Inject
    NearbyListPresenter(Activity activity) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();

        mSubscriber = new ForegroundSubscriber(mGoogleApiClient, new NearbyMessagesListener());
    }

    @Override
    public void onResume() {
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.connect();

        Subscription subscription = findConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bool -> mCmLinkEnabled = bool,
                        e -> LOGGER.error("Failed to find config settings.", e));

        reusableCompositeSubscription.add(subscription);
    }

    @Override
    public void onPause() {
        mGoogleApiClient.unregisterConnectionCallbacks(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mSubscriber.unSubscribe(mResultCallback);

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
        if (mScanning) {
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
        return mNearbyItemModels.size();
    }

    public void onShareSelected() {
        getView().showShareSheet();
    }

    public void onAddToCmFavorite() {
        List<String> nearbyIds = new ArrayList<>(mCheckedModels.size());

        for (NearbyItemModel model : mCheckedModels) {
            nearbyIds.add(model.mId);
        }

        Subscription subscription = Observable
                .from(nearbyIds)
                .flatMap(userId -> findCMJidUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable())
                .toList()
                .map(userIds -> cmFavoritesDataMapper.map(userIds))
                .flatMap(new Func1<CmFavoriteData, Observable<Timestamp>>() {
                    @Override
                    public Observable<Timestamp> call(CmFavoriteData data) {
                        return saveCMFavoritesUseCase.execute(data).subscribeOn(Schedulers.io()).toObservable();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timestamp -> {
                    for (NearbyItemModel model : mCheckedModels) {
                        model.mChecked = false;
                    }

                    mCheckedModels.clear();

                    getView().hideOptionMenu();
                    getView().drawNormalActionBarColor();
                    getView().updateItems();
                    getView().showSnackBar(R.string.message_added);
                }, e -> {
                    LOGGER.error("Failed to add nearby users to COMPANY Messenger.", e);
                    getView().showSnackBar(R.string.error_not_added);
                });

        reusableCompositeSubscription.add(subscription);
    }

    public void onSubscribe() {
        mSubscriber.subscribe(mResultCallback);

        mScanning = true;

        mExecutor.schedule(() -> {
            // Stop to scanning after 10 seconds.
            mSubscriber.unSubscribe(mResultCallback);

            mScanning = false;

            getView().hideIndicator();

            if (mNearbyItemModels.size() == 0) {
                getView().showSnackBar(R.string.message_not_found);
            }

            reusableCompositeSubscription.unSubscribe();
        }, 10, TimeUnit.SECONDS);
    }

    public boolean isCmLinkEnabled() {
        return mCmLinkEnabled;
    }

    public final class NearbyItemPresenter extends BaseItemPresenter<NearbyItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mNearbyItemModels.get(position));
        }

        public void onCheck(NearbyItemModel model) {
            if (model.mChecked) {
                Iterator<NearbyItemModel> iterator = mCheckedModels.iterator();
                while (iterator.hasNext()) {
                    NearbyItemModel nearbyItemModel = iterator.next();
                    if (nearbyItemModel.mId.equals(model.mId)) {
                        iterator.remove();
                    }
                }
            } else {
                mCheckedModels.add(model);
            }

            model.mChecked = !model.mChecked;

            if (0 < mCheckedModels.size()) {
                if (mCmLinkEnabled) {
                    getView().showOptionMenu();
                }
                getView().drawEditableActionBarColor();
            } else {
                getView().hideOptionMenu();
                getView().drawNormalActionBarColor();
            }
        }
    }
}
