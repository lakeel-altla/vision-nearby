package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllDeviceTokenUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveInformationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveNotificationUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


public class NotificationService extends IntentService {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindAllDeviceTokenUseCase findAllDeviceTokenUseCase;

    @Inject
    SaveNotificationUseCase saveNotificationUseCase;

    @Inject
    SaveInformationUseCase saveInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    // NOTE:
    // This constructor is need.
    public NotificationService() {
        this(NotificationService.class.getSimpleName());
    }

    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent component = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(getApplicationContext()))
                .build();
        component.inject(this);

        String beaconId = intent.getStringExtra(IntentKey.BEACON_ID.name());
        String userId = intent.getStringExtra(IntentKey.USER_ID.name());

        String title = getApplicationContext().getString(R.string.notification_title_device_found);
        String body = getApplicationContext().getString(R.string.notification_message_device_found);

        findBeaconUseCase.execute(beaconId)
                .subscribe(beacon -> {
                    if (!beacon.isLost) {
                        return;
                    }

                    // If the beacon is lost, notify token user.
                    findAllDeviceTokenUseCase.execute(userId)
                            // Filter other devices of the user.
                            .filter(token -> !beaconId.equals(token.beaconId))
                            // Notify the push notification token the device.
                            .subscribe(deviceToken -> saveNotification(deviceToken.token, title, body),
                                    e -> {
                                        LOGGER.error("Failed.", e);
                                    });

                    saveInformationUseCase.execute(userId, title, body)
                            .toObservable()
                            .subscribe(o -> {
                            }, e -> {
                                LOGGER.error("Failed.", e);
                            });
                }, e -> LOGGER.error("Failed.", e));
    }

    private void saveNotification(String token, String title, String message) {
        saveNotificationUseCase.execute(token, title, message).subscribe();
    }
}