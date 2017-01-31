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
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserActivityUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserLocationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveWeatherUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.awareness.AwarenessException;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;

public class HistoryService extends IntentService {

    private class ConnectionCallback implements GoogleApiClient.ConnectionCallbacks {

        private final Context context;

        private final String userId;

        private final String regionState;

        ConnectionCallback(Context context, String userId, String regionState) {
            this.context = context;
            this.userId = userId;
            this.regionState = regionState;
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            findUserUseCase.execute(userId)
                    .toObservable()
                    // Analytics
                    .doOnNext(user -> analyticsReporter.addHistory(user.userId, user.name))
                    .flatMap(user -> saveHistory(user.userId, regionState))
                    .subscribe(uniqueId -> {
                        getUserActivity()
                                .subscribe(userActivity -> saveUserActivity(uniqueId, userActivity), new ErrorAction<>());

                        getUserLocation(context)
                                .subscribe(location -> saveUserLocation(uniqueId, location), new ErrorAction<>());

                        getWeather(context)
                                .subscribe(weather -> saveWeather(uniqueId, weather), new ErrorAction<>());
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
        String userId = intent.getStringExtra(IntentKey.USER_ID.name());
        String regionState = intent.getStringExtra(IntentKey.REGION.name());

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new ConnectionCallback(context, userId, regionState))
                .build();

        googleApiClient.connect();
    }

    private Observable<String> saveHistory(String passingUserId, String regionState) {
        return saveHistoryUseCase.execute(passingUserId, regionState).toObservable();
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

    private void saveUserActivity(String uniqueId, DetectedActivity userActivity) {
        saveUserActivityUseCase.execute(uniqueId, userActivity)
                .subscribe();
    }

    private void saveUserLocation(String uniqueId, Location location) {
        saveUserLocationUseCase.execute(uniqueId, location).subscribe();
    }

    private void saveWeather(String uniqueId, Weather weather) {
        saveWeatherUseCase.execute(uniqueId, weather).subscribe();
    }
}