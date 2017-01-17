package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferencesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindSubscribeSettingUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.OfflineUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SavePreferenceBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveTokenUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningService;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> {

    @Inject
    SavePreferenceBeaconIdUseCase saveBeaconIdUseCase;

    @Inject
    ObserveConnectionUseCase observeConnectionUseCase;

    @Inject
    FindBeaconIdUseCase findBeaconIdUseCase;

    @Inject
    FindPreferencesUseCase findPreferencesUseCase;

    @Inject
    SaveUserBeaconUseCase saveUserBeaconUseCase;

    @Inject
    SaveBeaconUseCase saveBeaconUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    @Inject
    OfflineUseCase offlineUseCase;

    @Inject
    FindSubscribeSettingUseCase findSubscribeSettingUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();

    private final Context context;

    private boolean isAdvertiseAvailability = true;

    @Inject
    ActivityPresenter(Activity activity) {
        context = activity.getApplicationContext();
    }

    @Override
    public void onCreateView(ActivityView activityView) {
        super.onCreateView(activityView);

        if (MyUser.isAuthenticated()) {
            onSignIn();
        } else {
            getView().showSignInFragment();
        }
    }

    public void onSignIn() {
        getView().showFavoriteListFragment();

        checkBle();
        showProfile();

        // Observe user presence.
        observeConnectionUseCase.execute(MyUser.getUid());

        Subscription subscription = findSubscribeSettingUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSubscribeEnabled -> {
                    if (isSubscribeEnabled) {
                        startMonitorBeacons();
                    } else {
                        stopMonitorBeacons();
                    }
                }, e -> LOGGER.error("Failed to find preference settings.", e));
        subscriptions.add(subscription);

        // TODO: UseCase
        Subscription subscription1 = findBeaconIdUseCase
                .execute(MyUser.getUid())
                .flatMap(beaconId -> {
                    if (StringUtils.isEmpty(beaconId)) {
                        return saveBeaconId(MyUser.getUid());
                    } else {
                        return Single.just(beaconId);
                    }
                })
                .flatMap(beaconId -> saveUserBeacon(MyUser.getUid(), beaconId))
                .flatMap(beaconId -> saveBeacon(beaconId, MyUser.getUid()))
                .flatMap(beaconId -> saveToken(MyUser.getUid(), beaconId))
                .flatMap(s -> findPreferences(MyUser.getUid()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    if (entity.isAdvertiseInBackgroundEnabled && isAdvertiseAvailability) {
                        getView().startAdvertiseService(entity.beaconId);
                    }
                }, e -> LOGGER.error("Failed to process.", e));
        subscriptions.add(subscription1);
    }

    public void onBleEnabled() {
        Subscription subscription = findBeaconIdUseCase.execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beaconId -> getView().startAdvertiseService(beaconId),
                        e -> LOGGER.error("Failed to find beacon ID."));
        subscriptions.add(subscription);
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
                                    stopMonitorBeacons();
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

    private void checkBle() {
        BleChecker checker = new BleChecker(context);
        State state = checker.checkState();
        if (state == State.OFF) {
            isAdvertiseAvailability = false;
            getView().showBleEnabledActivity();
        } else if (state == State.SUBSCRIBE_ONLY) {
            isAdvertiseAvailability = false;
            getView().showAdvertiseDisableConfirmDialog();
        }
    }

    private void showProfile() {
        MyUser.UserData userData = MyUser.getUserData();
        getView().showProfile(userData.displayName, userData.email, userData.imageUri);
    }

    private void startMonitorBeacons() {
        getView().startMonitorBeacons();
    }

    private void stopMonitorBeacons() {
        getView().stopMonitorBeacons();
    }

    private Single<String> saveBeaconId(String userId) {
        return saveBeaconIdUseCase.execute(userId).subscribeOn(Schedulers.io());
    }

    private Single<String> saveUserBeacon(String userId, String beaconId) {
        return saveUserBeaconUseCase.execute(userId, beaconId).subscribeOn(Schedulers.io());
    }

    private Single<String> saveBeacon(String beaconId, String userId) {
        return saveBeaconUseCase.execute(beaconId, userId, Build.MODEL).subscribeOn(Schedulers.io());
    }

    private Single<String> saveToken(String userId, String beaconId) {
        // TODO: Observable
        String token = instanceId.getToken();
        return saveTokenUseCase.execute(userId, beaconId, token).subscribeOn(Schedulers.io());
    }

    private Single<PreferenceEntity> findPreferences(String userId) {
        return findPreferencesUseCase.execute(userId).subscribeOn(Schedulers.io());
    }
}
