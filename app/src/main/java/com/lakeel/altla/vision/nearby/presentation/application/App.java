package com.lakeel.altla.vision.nearby.presentation.application;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.beacon.BeaconClient;
import com.lakeel.altla.vision.nearby.presentation.di.component.ApplicationComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerApplicationComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ApplicationModule;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.danlew.android.joda.JodaTimeAndroid;

public class App extends Application {

    private FirebaseDatabase firebaseDatabase;

    private ApplicationComponent applicationComponent;

    private BeaconClient beaconClient;

    public static ApplicationComponent getApplicationComponent(@NonNull Activity activity) {
        return ((App) activity.getApplication()).applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        MultiDex.install(this);

        JodaTimeAndroid.init(this);

        initImageLoader();

        beaconClient = new BeaconClient(this);
    }

    public void startMonitorBeacons() {
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
