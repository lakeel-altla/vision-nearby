package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lakeel.altla.cm.CmApplication;
import com.lakeel.altla.cm.config.AccessConfig;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCMLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferencesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTokenUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObservePresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.OfflineUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SavePreferenceBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveTokenUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmAuthConfigMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PreferencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningService;
import com.lakeel.altla.vision.nearby.presentation.subscriber.BackgroundSubscriber;
import com.lakeel.altla.vision.nearby.presentation.subscriber.Subscriber;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    AccessConfig accessConfig;

    @Inject
    SavePreferenceBeaconIdUseCase saveBeaconIdUseCase;

    @Inject
    ObservePresenceUseCase observePresenceUseCase;

    @Inject
    FindCMLinkUseCase findCmLinkUseCase;

    @Inject
    FindBeaconIdUseCase findBeaconIdUseCase;

    @Inject
    FindPreferencesUseCase findPreferencesUseCase;

    @Inject
    SaveUserBeaconUseCase saveUserBeaconUseCase;

    @Inject
    SaveBeaconUseCase saveBeaconUseCase;

    @Inject
    FindTokenUseCase findTokenUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    @Inject
    OfflineUseCase offlineUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();

    private PreferencesModelMapper preferencesModelMapper = new PreferencesModelMapper();

    private CmAuthConfigMapper cmAuthConfigMapper = new CmAuthConfigMapper();

    private final Context context;

    private final GoogleApiClient googleApiClient;

    private final Subscriber subscriber;

    private boolean isAccessLocationGranted;

    private boolean isAlreadySubscribed;

    private boolean isAdvertiseAvailability = true;

    @Inject
    ActivityPresenter(Activity activity) {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();
        context = activity.getApplicationContext();
        subscriber = new BackgroundSubscriber(context, googleApiClient);
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

    public void onStart() {
        if (MyUser.isAuthenticated() && isAccessLocationGranted()) {
            googleApiClient.registerConnectionCallbacks(this);
            googleApiClient.registerConnectionFailedListener(this);
            onConnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (MyUser.isAuthenticated()) {
            googleApiClient.unregisterConnectionCallbacks(this);
            googleApiClient.unregisterConnectionFailedListener(this);

            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LOGGER.info("Connected to nearby service.");

        Subscription subscription = findPreferencesUseCase
                .execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    if (entity.isSubscribeInBackgroundEnabled) {
                        getView().startSubscribeService();
//                        onSubscribeInBackground();
                    } else {
//                        onUnSubscribeInBackground();
                    }
                }, e -> LOGGER.error("Failed to find preference settings.", e));

        subscriptions.add(subscription);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getView().showConnectedResolutionSystemDialog(connectionResult);
    }

    public void onSignedIn() {
        getView().showFavoriteListFragment();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onAccessLocationGranted();
        } else {
            getView().showAccessFineLocationPermissionSystemDialog();
        }

        MyUser.UserData userData = MyUser.getUserData();
        getView().showProfile(userData.displayName, userData.email, userData.imageUri);

        BleChecker checker = new BleChecker(context);
        State state = checker.checkState();
        if (state == State.OFF) {
            getView().showBleEnabledActivity();
        } else if (state == State.SUBSCRIBE_ONLY) {
            isAdvertiseAvailability = false;
            getView().showAdvertiseDisableConfirmDialog();
        }

        observePresenceUseCase.execute(MyUser.getUid());

        Subscription beaconSubscription = findBeaconIdUseCase
                .execute(MyUser.getUid())
                .flatMap(beaconId -> {
                    if (StringUtils.isEmpty(beaconId))
                        return saveBeaconId(MyUser.getUid());
                    return Single.just(beaconId);
                })
                .flatMap(beaconId -> saveUserBeacon(MyUser.getUid(), beaconId))
                .flatMap(beaconId -> saveBeacon(beaconId, MyUser.getUid()))
                .flatMap(beaconId -> saveToken(MyUser.getUid(), beaconId))
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed to save beacon data.", e))
                .subscribe();
        subscriptions.add(beaconSubscription);

        Subscription preferenceSubscription = findPreferencesUseCase
                .execute(MyUser.getUid())
                .map(entity -> preferencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(model -> {
                    if (model.isAdvertiseInBackgroundEnabled && isAdvertiseAvailability) {
                        getView().startAdvertiseService(model.beaconId);
                    }
                }, e -> LOGGER.error("Failed to find preferences.", e));
        subscriptions.add(preferenceSubscription);

        Subscription cmLinksSubscription = findCmLinkUseCase
                .execute(MyUser.getUid())
                .map(entity -> cmAuthConfigMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(authConfig -> CmApplication.initialize(authConfig, accessConfig),
                        e -> LOGGER.error("Failed to initialize CM settings.", e));
        subscriptions.add(cmLinksSubscription);
    }

    public void onAccessLocationGranted() {
        isAccessLocationGranted = true;
        onConnect();
    }

    public boolean isAccessLocationGranted() {
        return isAccessLocationGranted;
    }

    public void onConnect() {
        googleApiClient.connect();
    }

    public void onSubscribeInBackground() {
        if (isAlreadySubscribed) {
            return;
        }

        subscriber.subscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {
                isAlreadySubscribed = false;
                getView().showResolutionSystemDialog(status);
            }
        });

        isAlreadySubscribed = true;
    }

    public void onUnSubscribeInBackground() {
        subscriber.unSubscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {
                getView().showResolutionSystemDialog(status);
            }
        });

        isAlreadySubscribed = false;
    }

    public void onSignOut(@NonNull Activity activity) {
        RunningService runningService = new RunningService(context, AdvertiseService.class);
        runningService.stop();

        Subscription subscription = offlineUseCase
                .execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> LOGGER.error("Failed to save offline status.", e),
                        () -> {
                            // Unless you explicitly sign out, sign-in state continues.
                            // If you want to sign out, it is necessary to both sign out FirebaseAuth and Play Service Auth.
                            Task<Void> task = AuthUI.getInstance().signOut(activity);
                            task.addOnCompleteListener(result -> {
                                if (result.isSuccessful()) {
                                    onUnSubscribeInBackground();
                                    getView().showSignInFragment();
                                } else {
                                    LOGGER.error("Failed to sign out.", result.getException());
                                    getView().showSnackBar(R.string.error_not_signed_out);
                                }
                            });

                            Exception e = task.getException();
                            if (e != null) {
                                LOGGER.error("Failed to sign out.", e);
                                getView().showSnackBar(R.string.error_not_signed_out);
                            }
                        });
        subscriptions.add(subscription);
    }

    Single<String> saveBeaconId(String userId) {
        return saveBeaconIdUseCase.execute(userId).subscribeOn(Schedulers.io());
    }

    Single<String> saveUserBeacon(String userId, String beaconId) {
        return saveUserBeaconUseCase.execute(userId, beaconId).subscribeOn(Schedulers.io());
    }

    Single<String> saveBeacon(String beaconId, String userId) {
        return saveBeaconUseCase.execute(beaconId, userId, Build.MODEL).subscribeOn(Schedulers.io());
    }

    Single<String> saveToken(String userId, String beaconId) {
        String token = instanceId.getToken();
        return saveTokenUseCase.execute(userId, beaconId, token).subscribeOn(Schedulers.io());
    }
}
