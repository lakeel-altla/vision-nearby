package com.lakeel.profile.notification.presentation.service;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.data.MyUser;
import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.data.entity.RecentlyEntity;
import com.lakeel.profile.notification.data.execption.DataStoreException;
import com.lakeel.profile.notification.data.mapper.RecentlyEntityMapper;
import com.lakeel.profile.notification.presentation.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

public class RecentlyService extends IntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentlyService.class);

    private static final String ITEMS_REFERENCE = "items";

    private static final String RECENTLY_REFERENCE = "recently";

    private GoogleApiClient mGoogleApiClient;

    private RecentlyEntityMapper mRecentlyEntityMapper = new RecentlyEntityMapper();

    public RecentlyService() {
        super(RecentlyService.class.getSimpleName());
    }

    public RecentlyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String userId = intent.getStringExtra(IntentKey.USER_ID.name());

        LOGGER.info("Nearby user was found:userId = " + userId);

        final Context context = getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Single<DetectedActivity> single1 = getUserCurrentActivity();
                        Single<Location> single2 = getUserCurrentLocation(context);
                        Single<Weather> single3 = getWeather(context);
                        Single<ItemsEntity> single4 = findItemById(userId);

                        // Save history.
                        Single.zip(single1, single2, single3, single4, (detectedActivity, location, weather, entity) ->
                                mRecentlyEntityMapper.map(entity.key, detectedActivity, location, weather))
                                .flatMap(entity -> saveRecently(entity).subscribeOn(Schedulers.io()))
                                .subscribe(usersEntity -> LOGGER.debug("Succeeded to save to recently."),
                                        e -> LOGGER.error("Failed to save to recently.", e));
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();

        mGoogleApiClient.connect();
    }

    Single<DetectedActivity> getUserCurrentActivity() {
        return Single.create(new Single.OnSubscribe<DetectedActivity>() {
            @Override
            public void call(SingleSubscriber<? super DetectedActivity> subscriber) {
                // Get user activity.
                Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                        .setResultCallback(detectedActivityResult -> {
                            if (!detectedActivityResult.getStatus().isSuccess()) {
                                subscriber.onSuccess(null);
                                return;
                            }

                            ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                            DetectedActivity probableActivity = ar.getMostProbableActivity();
                            subscriber.onSuccess(probableActivity);
                        });
            }
        });
    }

    Single<Location> getUserCurrentLocation(Context context) {
        return Single.create(subscriber -> {
            // Get user location.
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                        .setResultCallback(locationResult -> {
                            if (!locationResult.getStatus().isSuccess()) {
                                LOGGER.error("Could not get user location. User may be turning off the location.");
                                subscriber.onSuccess(null);
                                return;
                            }
                            Location location = locationResult.getLocation();
                            subscriber.onSuccess(location);
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    Single<Weather> getWeather(Context context) {
        return Single.create(subscriber -> {
            // Get weather data.
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                        .setResultCallback(weatherResult -> {
                            if (!weatherResult.getStatus().isSuccess()) {
                                LOGGER.error("Could not get weather data. User may be turning off the location.");
                                return;
                            }
                            Weather weather = weatherResult.getWeather();
                            subscriber.onSuccess(weather);
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    Single<ItemsEntity> findItemById(String id) {

        return Single.create(new Single.OnSubscribe<ItemsEntity>() {

            @Override
            public void call(SingleSubscriber<? super ItemsEntity> subscriber) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(ITEMS_REFERENCE);
                databaseReference.child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ItemsEntity entity = dataSnapshot.getValue(ItemsEntity.class);
                                entity.key = dataSnapshot.getKey();
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        });
            }
        });
    }

    Single<RecentlyEntity> saveRecently(RecentlyEntity entity) {
        return Single.create(subscriber -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(RECENTLY_REFERENCE);
            DatabaseReference postReference = databaseReference.child(MyUser.getUid()).push();

            Task<Void> task = postReference.setValue(entity.toMap())
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }
}
