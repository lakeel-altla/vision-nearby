package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.tasks.Task;

import com.firebase.ui.auth.AuthUI;
import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.config.AccessConfig;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.BeaconIdEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCMLinksUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferencesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObservePresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.checker.BleState;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CMAuthConfigMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PreferencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.service.PublishService;
import com.lakeel.altla.vision.nearby.presentation.service.ServiceManager;
import com.lakeel.altla.vision.nearby.presentation.subscriber.BackgroundSubscriber;
import com.lakeel.altla.vision.nearby.presentation.subscriber.Subscriber;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    AccessConfig mAccessConfig;

    @Inject
    SaveBeaconIdUseCase mSaveBeaconIdUseCase;

    @Inject
    ObservePresenceUseCase mObservePresenceUseCase;

    @Inject
    FindCMLinksUseCase mFindCMLinksUseCase;

    @Inject
    FindBeaconIdUseCase mFindBeaconIdUseCase;

    @Inject
    FindPreferencesUseCase mFindPreferencesUseCase;

    @Inject
    SaveUserBeaconUseCase mSaveUserBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private PreferencesModelMapper mPreferencesModelMapper = new PreferencesModelMapper();

    private CMAuthConfigMapper mCMAuthConfigMapper = new CMAuthConfigMapper();

    private final Context mContext;

    private final GoogleApiClient mGoogleApiClient;

    private final Subscriber mSubscriber;

    private boolean mAccessLocationGranted;

    private boolean mAlreadySubscribed;

    private boolean mPublishAvailability = true;

    @Inject
    ActivityPresenter(Activity activity) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();
        mContext = activity.getApplicationContext();
        mSubscriber = new BackgroundSubscriber(mContext, mGoogleApiClient);
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
        if (MyUser.isAuthenticated() && isAccessLocationGranted()) {
            mGoogleApiClient.registerConnectionCallbacks(this);
            mGoogleApiClient.registerConnectionFailedListener(this);

            onConnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (MyUser.isAuthenticated()) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);

            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LOGGER.info("Connected to nearby service.");

        Subscription subscription = mFindPreferencesUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    if (entity.isSubscribeInBackgroundEnabled) {
                        onSubscribeInBackground();
                    } else {
                        onUnSubscribeInBackground();
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
        getView().showFavoritesListFragment();

        mObservePresenceUseCase.execute();

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

        Subscription beaconIdSubscription = mFindBeaconIdUseCase
                .execute()
                .flatMap(new Func1<BeaconIdEntity, Single<String>>() {
                    @Override
                    public Single<String> call(BeaconIdEntity entity) {
                        if (entity == null) {
                            return mSaveBeaconIdUseCase
                                    .execute()
                                    .subscribeOn(Schedulers.io());
                        }
                        String beaconId = entity.namespaceId + entity.instanceId;
                        return Single.just(beaconId);
                    }
                })
                .flatMap(new Func1<String, Single<String>>() {
                    @Override
                    public Single<String> call(String beaconId) {
                        return mSaveUserBeaconUseCase
                                .execute(beaconId)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed to save beacon data.", e))
                .subscribe();

        mCompositeSubscription.add(beaconIdSubscription);

        Subscription preferenceSubscription = mFindPreferencesUseCase
                .execute()
                .map(entity -> mPreferencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(model -> {
                    if (model.mPublishInBackgroundEnabled && mPublishAvailability) {
                        getView().startPublishService(model);
                    }
                }, e -> LOGGER.error("Failed to find preferences.", e));

        mCompositeSubscription.add(preferenceSubscription);

        Subscription cmLinksSubscription = mFindCMLinksUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .map(entity -> mCMAuthConfigMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authConfig -> CMApplication.initialize(authConfig, mAccessConfig),
                        e -> LOGGER.error("Failed to initialize CM settings.", e));

        mCompositeSubscription.add(cmLinksSubscription);
    }

    public void onAccessLocationGranted() {
        mAccessLocationGranted = true;
        onConnect();
    }

    public boolean isAccessLocationGranted() {
        return mAccessLocationGranted;
    }

    public void onConnect() {
        mGoogleApiClient.connect();
    }

    public void onSubscribeInBackground() {
        if (mAlreadySubscribed) {
            return;
        }

        mSubscriber.subscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {
                mAlreadySubscribed = false;
                getView().showResolutionSystemDialog(status);
            }
        });

        mAlreadySubscribed = true;
    }

    public void onUnSubscribeInBackground() {
        mSubscriber.unSubscribe(new ResolutionResultCallback() {
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
                onUnSubscribeInBackground();

                ServiceManager manager = new ServiceManager(mContext, PublishService.class);
                manager.stopService();

                getView().showSignInFragment();
            } else {
                LOGGER.error("Failed to sign out", task1.getException());
                getView().showSnackBar(R.string.error_not_signed_out);
            }
        });
    }
}
