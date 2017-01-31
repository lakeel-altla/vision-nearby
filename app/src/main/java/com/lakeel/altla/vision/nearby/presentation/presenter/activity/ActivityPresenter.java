package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.beacon.EddystoneUid;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferencesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveUserProfileUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.OfflineUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveTokenUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.ActivityModelMapper;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningServiceManager;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;
import com.lakeel.altla.vision.nearby.rx.EmptyAction;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    ObserveUserProfileUseCase observeUserProfileUseCase;

    @Inject
    ObserveConnectionUseCase observeConnectionUseCase;

    @Inject
    FindPreferencesUseCase findPreferencesUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    @Inject
    OfflineUseCase offlineUseCase;

    @Inject
    SaveBeaconUseCase saveBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private final FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();

    private final ActivityModelMapper modelMapper = new ActivityModelMapper();

    private final Context context;

    private boolean isAdvertiseAvailableDevice = true;

    @Inject
    ActivityPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreateView(ActivityView activityView) {
        super.onCreateView(activityView);

        if (MyUser.isAuthenticated()) {
            postSignIn();
        } else {
            getView().showSignInFragment();
        }
    }

    public void postSignIn() {
        getView().showFavoriteListFragment();

        showDrawerProfile();
        checkDeviceBle();

        // Observe user presence.
        observeConnectionUseCase.execute(MyUser.getUserId());

        // Observe user profile
        observeUserProfileUseCase.execute()
                .map(modelMapper::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().updateProfile(model);
                }, new ErrorAction<>());

        Subscription subscription = findPreferencesUseCase.execute()
                .observeOn(Schedulers.io())
                .subscribe(preference -> {
                    String beaconId = preference.beaconId;
                    if (StringUtils.isEmpty(beaconId)) {
                        EddystoneUid eddystoneUid = new EddystoneUid();
                        // Create EddystoneUID.
                        beaconId = eddystoneUid.getBeaconId();
                        saveBeacon(beaconId);
                    }
                    saveToken(beaconId);
                    startAdvertiseIfNeeded(beaconId);
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onBleEnabled() {
        Subscription subscription = findPreferencesUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(preference -> {
                    if (preference.isSubscribeInBackgroundEnabled) {
                        getView().startDetectBeaconsInBackground();
                    }

                    String beaconId = preference.beaconId;
                    if (preference.isAdvertiseInBackgroundEnabled && isAdvertiseAvailableDevice) {
                        getView().startAdvertise(beaconId);
                    }
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onSignOut(@NonNull Activity activity) {
        Subscription subscription = offlineUseCase.execute().observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> new ErrorAction<>(),
                        () -> {
                            MyUser.UserProfile userProfile = MyUser.getUserProfile();

                            // Unless you explicitly sign out, sign-in state continues.
                            // If you want to sign out, it is necessary to both sign out FirebaseAuth and Play Service Auth.
                            Task<Void> task = AuthUI.getInstance().signOut(activity);
                            task.addOnCompleteListener(result -> {
                                if (result.isSuccessful()) {
                                    analyticsReporter.logout(userProfile.userId, userProfile.userName);

                                    RunningServiceManager runningServiceManager = new RunningServiceManager(context, AdvertiseService.class);
                                    runningServiceManager.stopService();

                                    stopDetectBeaconsInBackground();
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

    private void checkDeviceBle() {
        BleChecker checker = new BleChecker(context);
        State state = checker.checkState();

        if (state == State.ENABLE) {
            getView().startDetectBeaconsInBackground();
        } else if (state == State.OFF) {
            isAdvertiseAvailableDevice = false;
            getView().showBleEnabledActivity();
        } else if (state == State.SUBSCRIBE_ONLY) {
            isAdvertiseAvailableDevice = false;
            getView().startDetectBeaconsInBackground();
            getView().showAdvertiseDisableConfirmDialog();
        }

        // Set user property.
        analyticsReporter.setBleProperty(state);
    }

    private void showDrawerProfile() {
        getView().showDrawerProfile(MyUser.getUserProfile());
    }

    private void saveBeacon(String beaconId) {
        Subscription subscription = saveBeaconUseCase.execute(beaconId)
                .subscribe(new EmptyAction<>(), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void saveToken(String beaconId) {
        Subscription subscription = saveTokenUseCase.execute(beaconId, instanceId.getToken())
                .subscribe(new EmptyAction<>(), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void startAdvertiseIfNeeded(String beaconId) {
        Subscription subscription = findPreferencesUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(preference -> {
                    if (preference.isAdvertiseInBackgroundEnabled && isAdvertiseAvailableDevice) {
                        getView().startAdvertise(beaconId);
                    }
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void stopDetectBeaconsInBackground() {
        getView().stopDetectBeaconsInBackground();
    }
}
