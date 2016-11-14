package com.lakeel.profile.notification.presentation.presenter.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.tasks.Task;

import com.firebase.ui.auth.AuthUI;
import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.config.AccessConfig;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.data.entity.BeaconIdEntity;
import com.lakeel.profile.notification.data.entity.CMLinksEntity;
import com.lakeel.profile.notification.data.entity.PreferencesEntity;
import com.lakeel.profile.notification.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.profile.notification.domain.usecase.FindCMLinksUseCase;
import com.lakeel.profile.notification.domain.usecase.FindPreferencesUseCase;
import com.lakeel.profile.notification.domain.usecase.ObserveConnectionUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveBeaconIdUseCase;
import com.lakeel.profile.notification.presentation.checker.BleState;
import com.lakeel.profile.notification.presentation.checker.BluetoothChecker;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.CMAuthConfigMapper;
import com.lakeel.profile.notification.presentation.presenter.mapper.PreferencesModelMapper;
import com.lakeel.profile.notification.presentation.presenter.model.PreferencesModel;
import com.lakeel.profile.notification.presentation.receiver.NearbyReceiver;
import com.lakeel.profile.notification.presentation.view.ActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    @Named("CMHost")
    String CMHost;

    @Inject
    @Named("CMPort")
    int CMPort;

    @Inject
    GoogleApiClient mNearbyClient;

    @Inject
    SaveBeaconIdUseCase mSaveBeaconIdUseCase;

    @Inject
    ObserveConnectionUseCase mObserveConnectionUseCase;

    @Inject
    FindCMLinksUseCase mFindCMLinksUseCase;

    @Inject
    FindBeaconIdUseCase mFindBeaconIdUseCase;

    @Inject
    FindPreferencesUseCase mFindPreferencesUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private Context mContext;

    private PreferencesModel mPreferencesModel;

    private boolean mAccessLocationGranted;

    private boolean mAlreadySubscribed;

    private boolean mPublishAvailability = true;

    private PreferencesModelMapper mPreferencesModelMapper = new PreferencesModelMapper();

    private CMAuthConfigMapper mCMAuthConfigMapper = new CMAuthConfigMapper();

    @Inject
    ActivityPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void onCreateView(ActivityView activityView) {
        super.onCreateView(activityView);

        if (MyUser.isAuthenticated()) {
            onSignedIn();
        } else {
            getView().showSignInFragment();
        }
    }

    @Override
    public void onStart() {
        if (MyUser.isAuthenticated()) {
            if (isAccessLocationGranted()) {
                onConnect();
            }
        }
    }

    @Override
    public void onResume() {
        mNearbyClient.registerConnectionCallbacks(this);
        mNearbyClient.registerConnectionFailedListener(this);
    }

    @Override
    public void onPause() {
        mNearbyClient.unregisterConnectionCallbacks(this);
        mNearbyClient.unregisterConnectionFailedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (MyUser.isAuthenticated()) {
            mNearbyClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LOGGER.info("Connected to nearby service.");

        Subscription subscription = mFindPreferencesUseCase
                .execute()
                .map(entity -> mPreferencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.mSubscribeInBackground) {
                        onSubscribe();
                    } else {
                        onUnSubscribe();
                    }
                }, e -> LOGGER.error("Failed to find preference settings.", e));
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getView().showConnectedResolutionSystemDialog(connectionResult);
    }

    public void onSignedIn() {
        mObserveConnectionUseCase.execute();

        MyUser.UserData userData = MyUser.getUserData();
        getView().showProfile(userData.mDisplayName, userData.mEmail, userData.mImageUri);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onAccessLocationGranted();
        } else {
            getView().showAccessFineLocationPermissionSystemDialog();
        }

        BluetoothChecker checker = new BluetoothChecker(mContext);
        BleState state = checker.getState();
        if (state == BleState.OFF) {
            getView().showBleEnabledActivity();
        } else if (state == BleState.SUBSCRIBE_ONLY) {
            mPublishAvailability = false;
            getView().showPublishDisableDialog();
        }

        Subscription subscription = mFindBeaconIdUseCase
                .execute()
                .flatMap(new Func1<BeaconIdEntity, Single<String>>() {
                    @Override
                    public Single<String> call(BeaconIdEntity entity) {
                        if (entity == null) return mSaveBeaconIdUseCase.execute();

                        String beaconId = entity.namespaceId + entity.instanceId;
                        return Single.just(beaconId);
                    }
                }).flatMap(new Func1<String, Single<PreferencesEntity>>() {
                    @Override
                    public Single<PreferencesEntity> call(String o) {
                        return mFindPreferencesUseCase.execute();
                    }
                }).flatMap(new Func1<PreferencesEntity, Single<CMLinksEntity>>() {
                    @Override
                    public Single<CMLinksEntity> call(PreferencesEntity entity) {
                        mPreferencesModel = mPreferencesModelMapper.map(entity);
                        return mFindCMLinksUseCase.execute();
                    }
                }).map(entity -> mCMAuthConfigMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authConfig -> {
                    CMApplication.initialize(authConfig, new AccessConfig(CMHost, CMPort));

                    if (mPreferencesModel.mPublishInBackground && mPublishAvailability) {
                        getView().startPublishInService(mPreferencesModel);
                    }

                    getView().showFavoritesListFragment();
                }, e -> LOGGER.error("Failed to process.", e));
        mCompositeSubscription.add(subscription);
    }

    public void onAccessLocationGranted() {
        mAccessLocationGranted = true;
        onConnect();
    }

    public boolean isAccessLocationGranted() {
        return mAccessLocationGranted;
    }

    public void onConnect() {
        mNearbyClient.connect();
    }

    public void onSubscribe() {
        if (mAlreadySubscribed) {
            return;
        }

        PendingIntent intent = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, NearbyReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Nearby.Messages.subscribe(mNearbyClient, intent)
                .setResultCallback(new ResolutionResultCallback() {
                    @Override
                    protected void onResolution(Status status) {
                        mAlreadySubscribed = false;
                        getView().showResolutionSystemDialog(status);
                    }
                });

        mAlreadySubscribed = true;
    }

    public void onUnSubscribe() {
        PendingIntent intent = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, NearbyReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Nearby.Messages.unsubscribe(mNearbyClient, intent)
                .setResultCallback(new ResolutionResultCallback() {
                    @Override
                    protected void onResolution(Status status) {
                        getView().showResolutionSystemDialog(status);
                    }
                });

        mAlreadySubscribed = false;
    }

    public void onSignOut(@NonNull Activity activity) {

        // Unless you explicitly sign out, sign-in state continues.
        // If you want to sign out, it is necessary to both sign out FirebaseAuth and Play Service Auth.

        Task<Void> task = AuthUI.getInstance().signOut(activity);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                getView().showSignInFragment();
            } else {
                LOGGER.error("Failed to sign out", task1.getException());
                getView().showSnackBar(R.string.error_not_signed_out);
            }
        });
    }
}
