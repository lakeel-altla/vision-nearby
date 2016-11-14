package com.lakeel.profile.notification.presentation.application;

import com.google.firebase.database.FirebaseDatabase;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.di.component.ApplicationComponent;
import com.lakeel.profile.notification.presentation.di.component.DaggerApplicationComponent;
import com.lakeel.profile.notification.presentation.di.module.ApplicationModule;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.danlew.android.joda.JodaTimeAndroid;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

public class App extends Application {

    private FirebaseDatabase mFirebaseDatabase;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mFirebaseDatabase == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseDatabase.setPersistenceEnabled(true);
        }

        MultiDex.install(this);
        JodaTimeAndroid.init(this);
        initImageLoaderInstance();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        // 注意:
        // 別アプリがインストールされるため、リリース時にはコメントアウトすること。
//        LeakCanary.install(this);
    }

    public static ApplicationComponent getApplicationComponent(@NonNull Activity activity) {
        return ((App) activity.getApplication()).mApplicationComponent;
    }

    private void initImageLoaderInstance() {
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
