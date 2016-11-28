package com.lakeel.altla.vision.nearby.presentation.presenter.nearby;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;

import com.lakeel.altla.library.AttachmentListener;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindItemUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUsersToCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.constants.AttachmentType;
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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

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

            Subscription subscription = mFindItemUseCase
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
    FindItemUseCase mFindItemUseCase;

    @Inject
    SaveUsersToCmFavoritesUseCase mSaveUsersToCmFavoritesUseCase;

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    private static Logger LOGGER = LoggerFactory.getLogger(NearbyListPresenter.class);

    private final GoogleApiClient mGoogleApiClient;

    private final Subscriber mSubscriber;

    private final List<NearbyItemModel> mNearbyItemModels = new ArrayList<>();

    private final List<NearbyItemModel> mCheckedModels = new LinkedList<>();

    private NearbyItemsModelMapper mMapper = new NearbyItemsModelMapper();

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

        mSubscriber = new ForegroundSubscriber(mGoogleApiClient, new NearbyMessagesListener(), null);
    }

    @Override
    public void onResume() {
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.connect();

        Subscription subscription = mFindConfigsUseCase
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

        Subscription subscription = mSaveUsersToCmFavoritesUseCase
                .execute(nearbyIds)
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
                    LOGGER.error("Failed to add nearby items to CM.", e);
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

            reusableCompositeSubscription.unsubscribe();
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
