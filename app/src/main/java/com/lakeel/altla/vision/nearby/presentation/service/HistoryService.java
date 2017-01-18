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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveDetectedActivityUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserLocationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveWeatherUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsEvent;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsParam;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.exception.AwarenessException;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.analytics.UserParam;

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
            findBeaconUseCase
                    .execute(beaconId)
                    .subscribeOn(Schedulers.io())
                    .flatMap(entity -> findUser(entity.userId))
                    .toObservable()
                    .doOnNext(HistoryService.this::reportAnalytics)
                    .flatMap(entity -> saveHistory(MyUser.getUid(), entity.userId))
                    .subscribeOn(Schedulers.io())
                    .subscribe(uniqueKey -> {
                        getUserActivity()
                                .flatMap(detectedActivity -> saveDetectedActivityUseCase.execute(uniqueKey, MyUser.getUid(), detectedActivity).subscribeOn(Schedulers.io()))
                                .subscribeOn(Schedulers.io())
                                .subscribe(entity -> {
                                }, e -> LOGGER.error("Failed to save user activity.", e));

                        getUserLocation(context)
                                .flatMap(location -> saveUserLocationUseCase.execute(uniqueKey, MyUser.getUid(), location).subscribeOn(Schedulers.io()))
                                .subscribeOn(Schedulers.io())
                                .subscribe(entity -> {
                                }, e -> LOGGER.error("Failed to save user current location.", e));

                        getWeather(context)
                                .flatMap(weather -> saveWeatherUseCase.execute(uniqueKey, MyUser.getUid(), weather).subscribeOn(Schedulers.io()))
                                .subscribeOn(Schedulers.io())
                                .subscribe(entity -> {
                                }, e -> LOGGER.error("Failed to save weather.", e));
                    }, e -> LOGGER.error("Failed to save to recently.", e));
        }

        @Override
        public void onConnectionSuspended(int i) {
        }
    }

    @Inject
    FirebaseAnalytics firebaseAnalytics;

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    SaveHistoryUseCase saveHistoryUseCase;

    @Inject
    SaveUserLocationUseCase saveUserLocationUseCase;

    @Inject
    SaveDetectedActivityUseCase saveDetectedActivityUseCase;

    @Inject
    SaveWeatherUseCase saveWeatherUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

    private GoogleApiClient googleApiClient;

    public HistoryService() {
        // This constructor is need.
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

    private Observable<String> saveHistory(String myUserId, String otherUserId) {
        return saveHistoryUseCase.execute(myUserId, otherUserId).subscribeOn(Schedulers.io()).toObservable();
    }

    private void reportAnalytics(UserEntity entity) {
        // Analytics
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.HISTORY_USER_ID.getValue(), entity.userId);
        userParam.putString(AnalyticsParam.HISTORY_USER_NAME.getValue(), entity.name);
        firebaseAnalytics.logEvent(AnalyticsEvent.ADD_HISTORY.getValue(), userParam.toBundle());
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
}
