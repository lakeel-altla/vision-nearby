package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.beacon.EddystoneUid;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveUserProfileUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.OfflineUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveDeviceTokenUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLastUsedDeviceTimeUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.ble.checker.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.checker.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.ActivityModelMapper;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningServiceManager;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

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
    FindPreferenceUseCase findPreferenceUseCase;

    @Inject
    SaveLastUsedDeviceTimeUseCase saveLastUsedDeviceTimeUseCase;

    @Inject
    SaveDeviceTokenUseCase saveDeviceTokenUseCase;

    @Inject
    OfflineUseCase offlineUseCase;

    @Inject
    SaveBeaconUseCase saveBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    // For observeProfile subscription.
    private final ReusableCompositeSubscription observeSubscriptions = new ReusableCompositeSubscription();

    private boolean isAdvertiseAvailableDevice = true;

    private boolean isAccessFineLocationGranted = false;

    private boolean isAlreadyAdvertised;

    private final Context context;

    @Inject
    ActivityPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreateView(ActivityView activityView) {
        super.onCreateView(activityView);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getView().showSignInFragment();
        } else {
            postSignIn();
        }
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void postSignIn() {
        getView().showFavoriteListFragment();
        getView().updateProfile(ActivityModelMapper.map());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Need token grant permission for subscribing beacons.
            checkAccessFineLocationPermission();
        } else {
            isAccessFineLocationGranted = true;
            checkDeviceBle();
        }

        // Observe user presence.
        observeConnectionUseCase.execute();

        // Observe user profile
        Subscription subscription = observeUserProfileUseCase.execute()
                .map(ActivityModelMapper::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().updateProfile(model);
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        observeSubscriptions.add(subscription);

        // TODO (Simply)
        Subscription subscription1 = findPreferenceUseCase.execute()
                .observeOn(Schedulers.io())
                .subscribe(preference -> {
                    String beaconId = preference.beaconId;
                    if (StringUtils.isEmpty(beaconId)) {
                        // Create a Eddystone-UID.
                        EddystoneUid eddystoneUid = new EddystoneUid();
                        beaconId = eddystoneUid.getBeaconId();
                    } else {
                        saveLastUsedDeviceTime(beaconId);
                    }

                    saveBeacon(beaconId);
                    saveToken(beaconId);
                    startAdvertiseInBackgroundIfNeeded();
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription1);
    }

    public void onBleEnabled() {
        // Check again whether can advertise.
        checkDeviceBle();
    }

    public void onAccessFineLocationGranted() {
        isAccessFineLocationGranted = true;
        checkDeviceBle();
    }

    public void onAccessFineLocationDenied() {
        isAccessFineLocationGranted = false;
        checkDeviceBle();
    }

    public void onSignOut(@NonNull Activity activity) {
        Subscription subscription = offlineUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                            LOGGER.error("Failed.", e);
                            getView().showSnackBar(R.string.snackBar_error_failed);
                        },
                        () -> {
                            observeSubscriptions.unSubscribe();

                            // Get user data here because do not get it after signing out.
                            FirebaseUser firebaseUser = CurrentUser.getUser();
                            String uid = firebaseUser.getUid();
                            String name = firebaseUser.getDisplayName();

                            // Unless you explicitly sign out, sign-in state continues.
                            // If you want token sign out, it is necessary token both sign out FirebaseAuth and Play Service Auth.

                            Task<Void> task = AuthUI.getInstance().signOut(activity);
                            task.addOnCompleteListener(result -> {
                                if (result.isSuccessful()) {
                                    analyticsReporter.logout(uid, name);

                                    RunningServiceManager serviceManager = new RunningServiceManager(context, AdvertiseService.class);
                                    serviceManager.stopService();

                                    stopDetectBeaconsInBackground();
                                    getView().finishActivity();
                                } else {
                                    LOGGER.error("Failed token sign out.", result.getException());
                                    getView().showSnackBar(R.string.snackBar_error_not_signed_out);
                                }
                            });

                            Exception e = task.getException();
                            if (e != null) {
                                LOGGER.error("Failed token sign out.", e);
                                getView().showSnackBar(R.string.snackBar_error_not_signed_out);
                            }
                        });
        subscriptions.add(subscription);
    }

    private void checkDeviceBle() {
        BleChecker checker = new BleChecker(context);
        State state = checker.checkState();

        if (state == State.ENABLE) {
            isAdvertiseAvailableDevice = true;
            startAdvertiseInBackgroundIfNeeded();

            if (isAccessFineLocationGranted) {
                startDetectBeaconsInBackgroundIfNeeded();
            }
        } else if (state == State.OFF) {
            isAdvertiseAvailableDevice = false;
            getView().showBleEnabledActivity();
        } else if (state == State.SUBSCRIBE_ONLY) {
            isAdvertiseAvailableDevice = false;
            getView().showAdvertiseDisableConfirmDialog();

            if (isAccessFineLocationGranted) {
                startDetectBeaconsInBackgroundIfNeeded();
            }
        } else {
            isAdvertiseAvailableDevice = false;
        }

        // Set user property.
        analyticsReporter.setBleProperty(state);
    }

    private void checkAccessFineLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                isAccessFineLocationGranted = true;
                checkDeviceBle();
            } else {
                getView().requestAccessFineLocationPermission();
            }
        }
    }

    private void startDetectBeaconsInBackgroundIfNeeded() {
        Subscription subscription = findPreferenceUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(preference -> {
                    if (preference.isSubscribeInBackgroundEnabled) {
                        getView().startDetectBeaconsInBackground();
                    }
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    private void saveBeacon(String beaconId) {
        Subscription subscription = saveBeaconUseCase.execute(beaconId)
                .subscribe(s -> {
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    private void saveLastUsedDeviceTime(String beaconId) {
        Subscription subscription = saveLastUsedDeviceTimeUseCase.execute(beaconId)
                .subscribe();
        subscriptions.add(subscription);
    }

    private void saveToken(String beaconId) {
        Subscription subscription = saveDeviceTokenUseCase.execute(beaconId)
                .subscribe(e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                }, () -> {
                });
        subscriptions.add(subscription);
    }

    private void startAdvertiseInBackgroundIfNeeded() {
        Subscription subscription = findPreferenceUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(preference -> {
                    if (StringUtils.isEmpty(preference.beaconId)) {
                        return;
                    }
                    if (preference.isAdvertiseInBackgroundEnabled && isAdvertiseAvailableDevice) {
                        if (isAlreadyAdvertised) {
                            return;
                        }
                        isAlreadyAdvertised = true;
                        getView().startAdvertise(preference.beaconId);
                    }
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    private void stopDetectBeaconsInBackground() {
        getView().stopDetectBeaconsInBackground();
    }
}