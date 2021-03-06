package com.lakeel.altla.vision.nearby.presentation.application;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferenceUseCase;
import com.lakeel.altla.vision.nearby.presentation.beacon.BeaconClient;
import com.lakeel.altla.vision.nearby.presentation.di.component.ApplicationComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerApplicationComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerDefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ApplicationModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.ContextModule;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;
import com.lakeel.altla.vision.nearby.presentation.view.activity.EmptyActivityLifecycleCallbacks;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.danlew.android.joda.JodaTimeAndroid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;

public class App extends Application {

    @Inject
    FindPreferenceUseCase findPreferenceUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private FirebaseDatabase firebaseDatabase;

    private ApplicationComponent applicationComponent;

    private BeaconClient beaconClient;

    @Override
    public void onCreate() {
        super.onCreate();

        LOGGER.info("App launch.");

        // NOTE:
        // Must invoke here before using Dagger2.
        MultiDex.install(this);

        // Output firebase logs.
        FirebaseDatabase.getInstance().setLogLevel(com.google.firebase.database.Logger.Level.DEBUG);

        DefaultComponent component = DaggerDefaultComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        component.inject(this);

        JodaTimeAndroid.init(this);
        initImageLoader();

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        beaconClient = new BeaconClient(App.this);

        // Even if the application is killed, this onCreate method is called by AltBeacon library.
        // In that case, need token start token monitor beacons here.
        if (CurrentUser.isSignedIn()) {
            subscribeInBackgroundIfNeeded();
        }

        registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                subscriptions.unSubscribe();
            }
        });
    }

    public static ApplicationComponent getApplicationComponent(@NonNull Activity activity) {
        return ((App) activity.getApplication()).applicationComponent;
    }

    public static void startDetectBeaconsInBackground(@NonNull Activity activity) {
        // When user enable ble, start token monitor beacons.
        App app = ((App) activity.getApplication());
        app.startDetectBeaconsInBackground();
    }

    public static void stopDetectBeaconsInBackground(@NonNull Activity activity) {
        App app = ((App) activity.getApplication());
        app.stopDetectBeaconsInBackground();
    }

    public static void startDetectBeaconsInBackground(@NonNull Fragment fragment) {
        // When user enable ble, start token monitor beacons.
        App.startDetectBeaconsInBackground(fragment.getActivity());
    }

    public static void stopDetectBeaconsInBackground(@NonNull Fragment fragment) {
        App.stopDetectBeaconsInBackground(fragment.getActivity());
    }

    public void startDetectBeaconsInBackground() {
        beaconClient.startDetectBeaconsInBackground();
    }

    public void stopDetectBeaconsInBackground() {
        beaconClient.stopDetectBeaconsInBackground();
    }

    private void initImageLoader() {
        // NOTE:
        // Not use vector drawables because not support it by Universal Image Loader.
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_account)
                .showImageOnFail(R.drawable.ic_account)
                .build();

        // When size of the cache is exceeded, remove the image that has not been used much.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void subscribeInBackgroundIfNeeded() {
        Subscription subscription = findPreferenceUseCase
                .execute()
                .subscribe(preference -> {
                    if (preference.isSubscribeInBackgroundEnabled) {
                        beaconClient.startDetectBeaconsInBackground();
                    }
                }, e -> LOGGER.error("Failed.", e));
        subscriptions.add(subscription);
    }
}