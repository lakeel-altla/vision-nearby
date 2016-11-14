package com.lakeel.profile.notification.presentation.presenter.nearby;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;

import com.lakeel.altla.library.AttachmentListener;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.data.MyUser;
import com.lakeel.profile.notification.domain.usecase.FindConfigsUseCase;
import com.lakeel.profile.notification.domain.usecase.FindItemUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveUsersToCmFavoritesUseCase;
import com.lakeel.profile.notification.presentation.constants.AttachmentType;
import com.lakeel.profile.notification.presentation.presenter.BaseItemPresenter;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.NearbyItemsModelMapper;
import com.lakeel.profile.notification.presentation.presenter.model.NearbyItemsModel;
import com.lakeel.profile.notification.presentation.view.NearbyItemView;
import com.lakeel.profile.notification.presentation.view.NearbyView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.IntRange;

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

public final class NearbyPresenter extends BasePresenter<NearbyView> {

    private class NearbyMessagesListner extends AttachmentListener {

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
                    .subscribeOn(Schedulers.io())
                    .map(itemsEntity -> mMapper.map(itemsEntity))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(scannedModel -> {
                        for (NearbyItemsModel model : mNearbyItemsModels) {
                            if (model.mId.equals(scannedModel.mId)) {
                                return;
                            }
                        }
                        mNearbyItemsModels.add(scannedModel);
                        getView().updateItems();
                    }, e -> LOGGER.error("Failed to find nearby item.", e));
            mCompositeSubscription.add(subscription);
        }
    }

    @Inject
    GoogleApiClient mNearbyClient;

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    SaveUsersToCmFavoritesUseCase mSaveUsersToCmFavoritesUseCase;

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    private static Logger LOGGER = LoggerFactory.getLogger(NearbyPresenter.class);

    private NearbyItemsModelMapper mMapper = new NearbyItemsModelMapper();

    private final List<NearbyItemsModel> mNearbyItemsModels = new ArrayList<>();

    private final List<NearbyItemsModel> mCheckedModels = new LinkedList<>();

    private NearbyMessagesListner mNearbyMessagesListner = new NearbyMessagesListner();

    private boolean mScanning;

    private boolean mCmLinkEnabled;

    @Inject
    NearbyPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_nearby);

        if (mNearbyClient.isConnected()) {
            getView().showIndicator();
            startSubscribe();
        }

        Subscription subscription = mFindConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bool -> mCmLinkEnabled = bool,
                        e -> LOGGER.error("Failed to find config settings.", e));
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onPause() {
        Nearby.Messages.unsubscribe(mNearbyClient, mNearbyMessagesListner);
    }

    @Override
    public void onStop() {
        super.onStop();
        getView().hideIndicator();
        getView().drawNormalActionBarColor();
    }

    public void onRefresh() {
        if (mScanning) {
            return;
        }

        startSubscribe();
    }

    public void onCreateItemView(NearbyItemView nearbyItemView) {
        NearbyItemPresenter nearbyItemPresenter = new NearbyItemPresenter();
        nearbyItemPresenter.onCreateItemView(nearbyItemView);
        nearbyItemView.setItemPresenter(nearbyItemPresenter);
    }

    public int getItemCount() {
        return mNearbyItemsModels.size();
    }

    public void onShare() {
        getView().showShareSheet();
    }

    public void onAddToCmFavorite() {
        List<String> nearbyIds = new ArrayList<>(mCheckedModels.size());

        for (NearbyItemsModel model : mCheckedModels) {
            nearbyIds.add(model.mId);
        }

        Subscription subscription = mSaveUsersToCmFavoritesUseCase
                .execute(nearbyIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timestamp -> {
                    for (NearbyItemsModel model : mCheckedModels) {
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
        mCompositeSubscription.add(subscription);
    }

    private void startSubscribe() {
        Nearby.Messages.subscribe(mNearbyClient, mNearbyMessagesListner)
                .setResultCallback(new ResolutionResultCallback() {
                    @Override
                    protected void onResolution(Status status) {
                        getView().showResolutionSystemDialog(status);
                    }
                });

        mScanning = true;

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> {
            // Stop to scanning after 3 seconds.
            Nearby.Messages.unsubscribe(mNearbyClient, mNearbyMessagesListner);
            mScanning = false;

            getView().hideIndicator();

            if (mNearbyItemsModels.size() == 0) {
                getView().showSnackBar(R.string.message_not_found);
            }
        }, 5, TimeUnit.SECONDS);
    }

    public boolean isCmLinkEnabled() {
        return mCmLinkEnabled;
    }

    public final class NearbyItemPresenter extends BaseItemPresenter<NearbyItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mNearbyItemsModels.get(position));
        }

        public void onCheck(NearbyItemsModel model) {
            if (model.mChecked) {
                Iterator<NearbyItemsModel> iterator = mCheckedModels.iterator();
                while (iterator.hasNext()) {
                    NearbyItemsModel nearbyItemsModel = iterator.next();
                    if (nearbyItemsModel.mId.equals(model.mId)) {
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
