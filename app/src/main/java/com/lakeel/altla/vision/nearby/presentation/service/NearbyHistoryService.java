package com.lakeel.altla.vision.nearby.presentation.service;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionType;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

public class NearbyHistoryService extends IntentService {

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

    // NOTE:
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

        String userId = intent.getStringExtra(IntentKey.USER_ID.name());
        RegionType regionType = RegionType.toRegionType(intent.getIntExtra(IntentKey.REGION_TYPE.name(), 0));

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Awareness.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        findUserUseCase.execute(userId)
                                .toObservable()
                                // Analytics
                                .doOnNext(userProfile -> analyticsReporter.addHistory(userProfile.userId, userProfile.name))
                                .doOnNext(userProfile -> {
                                    if (RegionType.ENTER == regionType) {
                                        showLocalNotification(userProfile);
                                    }
                                })
                                .flatMap(userProfile -> saveNearbyHistory(userProfile.userId, regionType))
                                .subscribe(nearbyHistoryId -> {
                                    getUserActivity()
                                            .subscribe(userActivity -> saveUserActivity(nearbyHistoryId, userActivity),
                                                    e -> LOGGER.error("Failed to save user activity.", e));

                                    getUserLocation(getApplicationContext())
                                            .subscribe(location -> saveUserLocation(nearbyHistoryId, location),
                                                    e -> LOGGER.error("Failed to save user location.", e));

                                    getWeather(getApplicationContext())
                                            .subscribe(weather -> saveWeather(nearbyHistoryId, weather),
                                                    e -> LOGGER.error("Failed to save weather.", e));
                                }, e -> LOGGER.error("Failed.", e));
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        LOGGER.info("Connection Suspended:cause=" + i);
                    }
                })
                .build();

        googleApiClient.connect();
    }

    private void showLocalNotification(UserProfile userProfile) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String title = getString(R.string.notification_title_app_user_found);
        String message = getString(R.string.notification_message_user_using_app, userProfile.name);

        LocalNotification localNotification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
        localNotification.show();
    }

    private Observable<String> saveNearbyHistory(String passingUserId, RegionType regionType) {
        return saveNearbyHistoryUseCase.execute(passingUserId, regionType).toObservable();
    }

    private Single<DetectedActivity> getUserActivity() {
        return Single.create(subscriber ->
                Awareness
                        .SnapshotApi
                        .getDetectedActivity(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                subscriber.onError(new AwarenessException("Could not get user activity."));
                            } else {
                                ActivityRecognitionResult activityRecognitionResult = result.getActivityRecognitionResult();
                                DetectedActivity probableActivity = activityRecognitionResult.getMostProbableActivity();
                                subscriber.onSuccess(probableActivity);
                            }
                        }));
    }

    private Single<Location> getUserLocation(@NonNull Context context) {
        return Single.create(subscriber -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness
                        .SnapshotApi
                        .getLocation(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                subscriber.onError(new AwarenessException("Could not get user location. User might be turning off the gps."));
                            } else {
                                Location location = result.getLocation();
                                subscriber.onSuccess(location);
                            }
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    private Single<Weather> getWeather(@NonNull Context context) {
        return Single.create(subscriber -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness
                        .SnapshotApi
                        .getWeather(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                subscriber.onError(new AwarenessException("Could not get weather data. User might be turning off the gps."));
                            } else {
                                Weather weather = result.getWeather();
                                subscriber.onSuccess(weather);
                            }
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    private void saveUserActivity(@NonNull String nearbyHistoryId, @NonNull DetectedActivity userActivity) {
        saveUserActivityUseCase.execute(nearbyHistoryId, userActivity)
                .subscribe(e -> LOGGER.error("Failed to save user activity.", e),
                        () -> {
                        });
    }

    private void saveUserLocation(@NonNull String nearbyHistoryId, @NonNull Location location) {
        saveUserLocationUseCase.execute(nearbyHistoryId, location)
                .subscribe(e -> LOGGER.error("Failed to save user location.", e),
                        () -> {
                        });
    }

    private void saveWeather(@NonNull String nearbyHistoryId, @NonNull Weather weather) {
        saveWeatherUseCase.execute(nearbyHistoryId, weather)
                .subscribe(e -> LOGGER.error("Failed to save weather.", e),
                        () -> {
                        });
    }
}