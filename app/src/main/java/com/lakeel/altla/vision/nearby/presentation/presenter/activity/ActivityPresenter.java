package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferencesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.ObserveUserProfileUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.OfflineUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveTokenUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.ServiceManager;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;
import com.lakeel.altla.vision.nearby.rx.EmptyAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> {

    @Inject
    ObserveUserProfileUseCase observeUserProfileUseCase;

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    ObserveConnectionUseCase observeConnectionUseCase;

    @Inject
    FindBeaconIdUseCase findBeaconIdUseCase;

    @Inject
    FindPreferencesUseCase findPreferencesUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    @Inject
    OfflineUseCase offlineUseCase;

    @Inject
    SaveBeaconUseCase saveBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private final Context context;

    private boolean isAdvertiseAvailability = true;

    @Inject
    ActivityPresenter(Context context) {
        this.context = context;
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

    public void onSignedIn() {
        getView().showFavoriteListFragment();

        checkBle();
        showProfile();

        // Observe user presence.
        observeConnectionUseCase.execute(MyUser.getUid());

        // Observe user profile
        observeUserProfileUseCase.execute()
                .map(entity -> userModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().updateProfile(model);
                }, e -> {
                    LOGGER.error("Failed to observe user profile.", e);
                });

        Subscription subscription = saveBeaconUseCase.execute().subscribe();
        subscriptions.add(subscription);

        Subscription subscription1 = saveTokenUseCase.execute(instanceId.getToken()).subscribe();
        subscriptions.add(subscription1);

        Subscription subscription2 = findPreferencesUseCase.execute()
                .subscribe(entity -> {
                    if (entity.isAdvertiseInBackgroundEnabled && isAdvertiseAvailability) {
                        getView().startAdvertiseService(entity.beaconId);
                    }
                }, new EmptyAction<>());
        subscriptions.add(subscription2);
    }

    public void onBleEnabled() {
        Subscription subscription = findBeaconIdUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beaconId -> getView().startAdvertiseService(beaconId),
                        e -> LOGGER.error("Failed to findList beacon ID."));
        subscriptions.add(subscription);
    }

    public void onSignOut(@NonNull Activity activity) {
        Subscription subscription = offlineUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> LOGGER.error("Failed to save offline status.", e),
                        () -> {
                            MyUser.UserData userData = MyUser.getUserData();

                            // Unless you explicitly sign out, sign-in state continues.
                            // If you want to sign out, it is necessary to both sign out FirebaseAuth and Play Service Auth.
                            Task<Void> task = AuthUI.getInstance().signOut(activity);
                            task.addOnCompleteListener(result -> {
                                if (result.isSuccessful()) {
                                    analyticsReporter.logout(userData.userId, userData.userName);

                                    ServiceManager serviceManager = new ServiceManager(context, AdvertiseService.class);
                                    serviceManager.stop();

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
        BleChecker.State state = checker.checkState();

        if (state == BleChecker.State.OFF) {
            isAdvertiseAvailability = false;
            getView().showBleEnabledActivity();
        } else if (state == BleChecker.State.SUBSCRIBE_ONLY) {
            isAdvertiseAvailability = false;
            getView().showAdvertiseDisableConfirmDialog();
        }

        analyticsReporter.setBleProperty(state);
    }

    private void showProfile() {
        MyUser.UserData userData = MyUser.getUserData();
        getView().showProfile(userData.userName, userData.email, userData.imageUri);
    }

    private void stopMonitorBeacons() {
        getView().stopMonitorBeacons();
    }
}
