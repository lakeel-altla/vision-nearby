package com.lakeel.altla.vision.nearby.presentation.application;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindSubscribeSettingsUseCase;
import com.lakeel.altla.vision.nearby.presentation.beacon.BeaconClient;
import com.lakeel.altla.vision.nearby.presentation.di.component.ApplicationComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerApplicationComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ApplicationModule;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class App extends Application {

    @Inject
    FindSubscribeSettingsUseCase findSubscribeSettingsUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private FirebaseDatabase firebaseDatabase;

    private ApplicationComponent applicationComponent;

    private BeaconClient beaconClient;

    public static ApplicationComponent getApplicationComponent(@NonNull Activity activity) {
        return ((App) activity.getApplication()).applicationComponent;
    }

    public static void startMonitorBeacons(Fragment fragment) {
        // When user enable ble, start to monitor beacons.
        App app = ((App) fragment.getActivity().getApplication());
        app.startMonitorBeacons();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LOGGER.debug("App launch.");

        MultiDex.install(this);
        JodaTimeAndroid.init(this);
        initImageLoader();

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        // TODO: Fix inject
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.viewComponent().inject(this);

        // Even if the application is killed, this onCreate method is called by AltBeacon library.
        // In that case, need to start to monitor beacons here.
        if (MyUser.isAuthenticated()) {
            Subscription subscription = findSubscribeSettingsUseCase.execute().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isEnabled -> {
                        beaconClient = new BeaconClient(this);
                        beaconClient.startMonitor();
                    }, e -> LOGGER.error("Failed to find subscribe settings.", e));
            subscriptions.add(subscription);
        }
    }

    public void startMonitorBeacons() {
        if (beaconClient == null) {
            beaconClient = new BeaconClient(this);
        }
        beaconClient.startMonitor();
    }

    public void stopMonitorBeacons() {
        beaconClient.stopMonitor();
    }

    private void initImageLoader() {
        // Init ImageLoader instance.
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_user)
                .showImageOnFail(R.mipmap.ic_user)
                .build();

        // When size of the cache is exceeded, remove the image that has not been used much.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
