package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTokensUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveInformationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveNotificationUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;
import com.lakeel.altla.vision.nearby.rx.EmptyAction;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Completable;


public class NotificationService extends IntentService {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    FindTokensUseCase findTokensUseCase;

    @Inject
    SaveNotificationUseCase saveNotificationUseCase;

    @Inject
    SaveInformationUseCase saveInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

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
        String message = getApplicationContext().getString(R.string.notification_message_device_found);



        findTokensUseCase.execute(userId)
                .filter(token -> !beaconId.equals(token.beaconId))
                .subscribe(entity -> saveNotification(entity.token, title, message), new ErrorAction<>());

        saveInformationUseCase.execute(userId, title, message)
                .toObservable()
                .subscribe(new EmptyAction<>(), new ErrorAction<>());
    }

    private Completable saveNotification(String token, String title, String message) {
        return saveNotificationUseCase.execute(token, title, message);
    }
}
