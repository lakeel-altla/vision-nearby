package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.data.entity.TokenEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTokensUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserIdByBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveInformationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveNotificationUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;


public class NotificationService extends IntentService {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindUserIdByBeaconIdUseCase findUserIdByBeaconIdUseCase;

    @Inject
    FindTokensUseCase findTokensUseCase;

    @Inject
    SaveNotificationUseCase saveNotificationUseCase;

    @Inject
    SaveInformationUseCase saveInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService() {
        // This constructor is need.
        this(NotificationService.class.getSimpleName());
    }

    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent component = DaggerServiceComponent.create();
        component.inject(this);

        String beaconId = intent.getStringExtra(IntentKey.BEACON_ID.name());
        String title = getApplicationContext().getString(R.string.notification_title_device_found);
        String message = getApplicationContext().getString(R.string.notification_message_device_found);

        Observable<String> observableForUserId = findBeaconUseCase
                .execute(beaconId)
                .toObservable()
                .filter(entity -> entity.isLost)
                .flatMap(entity -> findUserId(beaconId))
                .map(entity -> entity.userId);

        observableForUserId
                .flatMap(this::findTokens)
                .filter(entity -> !beaconId.equals(entity.beaconId))
                .flatMap(entity -> saveNotification(entity.token, title, message))
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed to save notification.", e))
                .subscribe();

        observableForUserId
                .flatMap(userId -> saveInformation(userId, title, message))
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed to save information.", e))
                .subscribe();
    }

    Observable<BeaconEntity> findUserId(String beaconId) {
        return findUserIdByBeaconIdUseCase.execute(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }

    Observable<TokenEntity> findTokens(String userId) {
        return findTokensUseCase.execute(userId).subscribeOn(Schedulers.io());
    }

    Observable<Object> saveInformation(String userId, String title, String message) {
        return saveInformationUseCase.execute(userId, title, message).toObservable();
    }

    Observable<NotificationEntity> saveNotification(String token, String title, String message) {
        return saveNotificationUseCase.execute(token, title, message).subscribeOn(Schedulers.io()).toObservable();
    }
}
