package com.lakeel.altla.vision.nearby.presentation.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserActivityUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.awareness.AwarenessException;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.rx.EmptyAction;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

public class HistoryService extends IntentService {

    private class ConnectionCallback implements GoogleApiClient.ConnectionCallbacks {

        private Context context;

        private String beaconId;

        ConnectionCallback(Context context, String beaconId) {
            this.context = context;
            this.beaconId = beaconId;
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            findBeaconUseCase.execute(beaconId)
                    .flatMap(entity -> findUser(entity.userId))
                    .toObservable()
                    // Analytics
                    .doOnNext(entity -> analyticsReporter.addHistory(entity.userId, entity.name))
                    .flatMap(entity -> saveHistory(entity.userId))
                    .subscribe(uniqueId -> {
                        getUserActivity()
                                .flatMap(userActivity -> saveUserActivity(uniqueId, userActivity))
                                .subscribeOn(Schedulers.io())
                                .subscribe(new EmptyAction<>(), new ErrorAction<>());

                        getUserLocation(context)
                                .flatMap(location -> saveUserLocation(uniqueId, location))
                                .subscribeOn(Schedulers.io())
                                .subscribe(new EmptyAction<>(), new ErrorAction<>());

                        getWeather(context)
                                .flatMap(weather -> saveWeather(uniqueId, weather))
                                .subscribeOn(Schedulers.io())
                                .subscribe(new EmptyAction<>(), new ErrorAction<>());
                    }, new ErrorAction<>());
        }

        @Override
        public void onConnectionSuspended(int i) {
        }
    }

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    SaveHistoryUseCase saveHistoryUseCase;

    @Inject
    SaveUserLocationUseCase saveUserLocationUseCase;

    @Inject
    SaveUserActivityUseCase saveUserActivityUseCase;

    @Inject
    SaveWeatherUseCase saveWeatherUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

    private GoogleApiClient googleApiClient;

    // This constructor is need.
    public HistoryService() {
        this(HistoryService.class.getSimpleName());
    }

    public HistoryService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent serviceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(getApplicationContext()))
                .build();
        serviceComponent.inject(this);

        Context context = getApplicationContext();
        String beaconId = intent.getStringExtra(IntentKey.BEACON_ID.name());

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new ConnectionCallback(context, beaconId))
                .build();

        googleApiClient.connect();
    }

    private Single<UserEntity> findUser(String userId) {
        return findUserUseCase.execute(userId).subscribeOn(Schedulers.io());
    }

    private Observable<String> saveHistory(String passingUserId) {
        return saveHistoryUseCase.execute(passingUserId).subscribeOn(Schedulers.io()).toObservable();
    }

    private Single<DetectedActivity> getUserActivity() {
        return Single.create(new Single.OnSubscribe<DetectedActivity>() {
            @Override
            public void call(SingleSubscriber<? super DetectedActivity> subscriber) {
                Awareness.SnapshotApi.getDetectedActivity(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                subscriber.onError(new AwarenessException("Could not get user activity."));
                                return;
                            }
                            ActivityRecognitionResult ar = result.getActivityRecognitionResult();
                            DetectedActivity probableActivity = ar.getMostProbableActivity();
                            subscriber.onSuccess(probableActivity);
                        });
            }
        });
    }

    private Single<Location> getUserLocation(Context context) {
        return Single.create(subscriber -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness.SnapshotApi.getLocation(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                subscriber.onError(new AwarenessException("Could not get user location. User might be turning off the gps."));
                                return;
                            }
                            Location location = result.getLocation();
                            subscriber.onSuccess(location);
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    private Single<Weather> getWeather(Context context) {
        return Single.create(subscriber -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness.SnapshotApi.getWeather(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                subscriber.onError(new AwarenessException("Could not get weather data. User might be turning off the gps."));
                                return;
                            }
                            Weather weather = result.getWeather();
                            subscriber.onSuccess(weather);
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    private Single<HistoryEntity> saveUserActivity(String uniqueId, DetectedActivity userActivity) {
        return saveUserActivityUseCase.execute(uniqueId, MyUser.getUserId(), userActivity).subscribeOn(Schedulers.io());
    }

    private Single<HistoryEntity> saveUserLocation(String uniqueId, Location location) {
        return saveUserLocationUseCase.execute(uniqueId, MyUser.getUserId(), location).subscribeOn(Schedulers.io());
    }

    private Single<HistoryEntity> saveWeather(String uniqueId, Weather weather) {
        return saveWeatherUseCase.execute(uniqueId, MyUser.getUserId(), weather).subscribeOn(Schedulers.io());
    }
}