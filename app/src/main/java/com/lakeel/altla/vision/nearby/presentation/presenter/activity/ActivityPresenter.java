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
import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.config.AccessConfig;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferenceBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCMLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferencesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTokenUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObservePresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SavePreferenceBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveTokensUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState;
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

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    AccessConfig accessConfig;

    @Inject
    SavePreferenceBeaconIdUseCase savePreferenceBeaconIdUseCase;

    @Inject
    ObservePresenceUseCase observePresenceUseCase;

    @Inject
    FindCMLinkUseCase findCMLinkUseCase;

    @Inject
    FindPreferenceBeaconIdUseCase findPreferenceBeaconIdUseCase;

    @Inject
    FindPreferencesUseCase findPreferencesUseCase;

    @Inject
    SaveUserBeaconUseCase saveUserBeaconUseCase;

    @Inject
    SaveBeaconUseCase saveBeaconUseCase;

    @Inject
    FindTokenUseCase findTokenUseCase;

    @Inject
    SaveTokensUseCase saveTokensUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();

    private PreferencesModelMapper preferencesModelMapper = new PreferencesModelMapper();

    private CMAuthConfigMapper cmAuthConfigMapper = new CMAuthConfigMapper();

    private final Context context;

    private final GoogleApiClient googleApiClient;

    private final Subscriber subscriber;

    private boolean accessLocationGranted;

    private boolean alreadySubscribed;

    private boolean publishAvailability = true;

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

    @Override
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

        reusableCompositeSubscription.add(subscription);
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

        observePresenceUseCase.execute(MyUser.getUid());

        MyUser.UserData userData = MyUser.getUserData();
        getView().showProfile(userData.displayName, userData.email, userData.imageUri);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onAccessLocationGranted();
        } else {
            getView().showAccessFineLocationPermissionSystemDialog();
        }

        BluetoothChecker checker = new BluetoothChecker(context);
        BleState state = checker.getState();
        if (state == BleState.OFF) {
            getView().showBleEnabledActivity();
        } else if (state == BleState.SUBSCRIBE_ONLY) {
            publishAvailability = false;
            getView().showPublishDisableDialog();
        }

        Subscription beaconSubscription = findPreferenceBeaconIdUseCase
                .execute()
                .flatMap(entity -> {
                    if (entity == null) {
                        return savePreferenceBeaconIdUseCase.execute().subscribeOn(Schedulers.io());
                    }
                    return Single.just(entity.namespaceId + entity.instanceId);
                })
                .flatMap(beaconId -> saveUserBeaconUseCase.execute(MyUser.getUid(), beaconId).subscribeOn(Schedulers.io()))
                .flatMap(beaconId -> saveBeaconUseCase.execute(beaconId, MyUser.getUid(), Build.MODEL).subscribeOn(Schedulers.io()))
                .flatMap(beaconId -> {
                    String token = instanceId.getToken();
                    return saveTokensUseCase.execute(MyUser.getUid(), beaconId, token).subscribeOn(Schedulers.io());
                })
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed to save beacon data.", e))
                .subscribe();
        reusableCompositeSubscription.add(beaconSubscription);

        Subscription preferenceSubscription = findPreferencesUseCase
                .execute()
                .map(entity -> preferencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(model -> {
                    if (model.mPublishInBackgroundEnabled && publishAvailability) {
                        getView().startPublishService(model);
                    }
                }, e -> LOGGER.error("Failed to find preferences.", e));
        reusableCompositeSubscription.add(preferenceSubscription);

        Subscription cmLinksSubscription = findCMLinkUseCase
                .execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .map(entity -> cmAuthConfigMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authConfig -> CMApplication.initialize(authConfig, accessConfig),
                        e -> LOGGER.error("Failed to initialize CM settings.", e));
        reusableCompositeSubscription.add(cmLinksSubscription);
    }

    public void onAccessLocationGranted() {
        accessLocationGranted = true;
        onConnect();
    }

    public boolean isAccessLocationGranted() {
        return accessLocationGranted;
    }

    public void onConnect() {
        googleApiClient.connect();
    }

    public void onSubscribeInBackground() {
        if (alreadySubscribed) {
            return;
        }

        subscriber.subscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {
                alreadySubscribed = false;
                getView().showResolutionSystemDialog(status);
            }
        });

        alreadySubscribed = true;
    }

    public void onUnSubscribeInBackground() {
        subscriber.unSubscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {
                getView().showResolutionSystemDialog(status);
            }
        });

        alreadySubscribed = false;
    }

    public void onSignOut(@NonNull Activity activity) {

        // Unless you explicitly sign out, sign-in state continues.
        // If you want to sign out, it is necessary to both sign out FirebaseAuth and Play Service Auth.

        Task<Void> task = AuthUI.getInstance().signOut(activity);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                onUnSubscribeInBackground();

                ServiceManager manager = new ServiceManager(context, PublishService.class);
                manager.stopService();

                getView().showSignInFragment();
            } else {
                LOGGER.error("Failed to sign out", task1.getException());
                getView().showSnackBar(R.string.error_not_signed_out);
            }
        });
    }
}
