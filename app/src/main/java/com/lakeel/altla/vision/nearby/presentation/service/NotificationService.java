package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserIdByBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTokensUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveNotificationUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.schedulers.Schedulers;


public class NotificationService extends IntentService {

    @Inject
    FindUserIdByBeaconIdUseCase findUserIdByBeaconIdUseCase;

    @Inject
    FindTokensUseCase findTokensUseCase;

    @Inject
    SaveNotificationUseCase saveNotificationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService() {
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

        findUserIdByBeaconIdUseCase
                .execute(beaconId)
                .flatMapObservable(entity -> findTokensUseCase.execute(entity.userId).subscribeOn(Schedulers.io()))
                .flatMap(token -> saveNotificationUseCase.execute(token, title, message).subscribeOn(Schedulers.io()).toObservable())
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed to save notification.", e))
                .subscribe();
    }
}
