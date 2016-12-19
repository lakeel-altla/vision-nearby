package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserIdByLineUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.intent.UriIntent;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LineService extends IntentService {

    @Inject
    FindUserIdByLineUrlUseCase findUserIdByLineUrlUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineService.class);

    public LineService() {
        this(LineService.class.getSimpleName());
    }

    public LineService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent component = DaggerServiceComponent.create();
        component.inject(this);

        String lineUrl = intent.getStringExtra(IntentKey.LINE_URL.name());

        LOGGER.info("LINE URL was found:URL=" + lineUrl);

        findUserIdByLineUrlUseCase
                .execute(lineUrl)
                .flatMap(entity -> findUser(entity.userId))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    String title = getString(R.string.notification_title_line_user_found);
                    String message = getString(R.string.notification_message_user_using_line, entity.name);

                    UriIntent uriIntent = new UriIntent(lineUrl);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, uriIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    LocalNotification notification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
                    notification.show();
                }, e -> LOGGER.error("Failed to notify a LINE notification.", e));
    }

    Single<UserEntity> findUser(String jsonKey) {
        return findUserUseCase.execute(jsonKey).subscribeOn(Schedulers.io());
    }
}
