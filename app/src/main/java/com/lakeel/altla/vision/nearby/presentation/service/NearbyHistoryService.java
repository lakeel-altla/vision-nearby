package com.lakeel.altla.vision.nearby.presentation.service;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
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
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserActivityUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserLocationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveWeatherUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.awareness.AwarenessException;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;
import com.lakeel.altla.vision.nearby.presentation.view.intent.DefaultIntent;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;

public class NearbyHistoryService extends IntentService {

    private class ConnectionCallback implements GoogleApiClient.ConnectionCallbacks {

        private final Context context;

        private final String userId;

        private final RegionState regionState;

        ConnectionCallback(Context context, String userId, RegionState regionState) {
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
                    .doOnNext(NearbyHistoryService.this::sendLocalNotification)
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
    SaveNearbyHistoryUseCase saveNearbyHistoryUseCase;

    @Inject
    SaveUserLocationUseCase saveUserLocationUseCase;

    @Inject
    SaveUserActivityUseCase saveUserActivityUseCase;

    @Inject
    SaveWeatherUseCase saveWeatherUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyHistoryService.class);

    private GoogleApiClient googleApiClient;

    // This constructor is need.
    public NearbyHistoryService() {
        this(NearbyHistoryService.class.getSimpleName());
    }

    public NearbyHistoryService(String name) {
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
        int regionState = intent.getIntExtra(IntentKey.REGION.name(), 0);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new ConnectionCallback(context, userId, RegionState.toRegionState(regionState)))
                .build();

        googleApiClient.connect();
    }

    private void sendLocalNotification(UserProfile userProfile) {
        String title = getString(R.string.notification_title_app_user_found);
        String message = getString(R.string.notification_message_user_using_app, userProfile.name);

        DefaultIntent defaultIntent = new DefaultIntent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, defaultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        LocalNotification localNotification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
        localNotification.show();
    }

    private Observable<String> saveHistory(String passingUserId, RegionState regionState) {
        return saveNearbyHistoryUseCase.execute(passingUserId, regionState).toObservable();
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
                                subscriber.onError(new AwarenessException("Could not get weather data. UserProfile might be turning off the gps."));
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
        saveUserActivityUseCase.execute(uniqueId, userActivity).subscribe();
    }

    private void saveUserLocation(String uniqueId, Location location) {
        saveUserLocationUseCase.execute(uniqueId, location).subscribe();
    }

    private void saveWeather(String uniqueId, Weather weather) {
        saveWeatherUseCase.execute(uniqueId, weather).subscribe();
    }
}