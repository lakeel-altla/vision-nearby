package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.intent.UriIntent;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public class LINEService extends IntentService {

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LINEService.class);

    // This constructor is need.
    public LINEService() {
        this(LINEService.class.getSimpleName());
    }

    public LINEService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent component = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(getApplicationContext()))
                .build();
        component.inject(this);

        String userId = intent.getStringExtra(IntentKey.USER_ID.name());

        // Show LINE URL notification.
        findUserUseCase.execute(userId)
                .subscribeOn(Schedulers.io())
                .subscribe(userEntity -> showLineNotification(userId, userEntity.name), e -> LOGGER.error("Failed to notify LINE notification.", e));
    }

    private void showLineNotification(String userId, String userName) {
        findLineLinkUseCase.execute(userId)
                .toObservable()
                .filter(entity -> entity != null)
                .subscribe(entity -> {
                    String title = getString(R.string.notification_title_line_user_found);
                    String message = getString(R.string.notification_message_user_using_line, userName);

                    UriIntent uriIntent = new UriIntent(entity.url);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, uriIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    LocalNotification notification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
                    notification.show();
                });
    }
}
