package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserIdByLineUrlUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.intent.UriPendingIntent;
import com.lakeel.altla.vision.nearby.presentation.view.notification.NotificationNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
        // Dagger
        ServiceComponent component = DaggerServiceComponent.builder().build();
        component.inject(this);

        String lineUrl = intent.getStringExtra(IntentKey.LINE_URL.name());

        LOGGER.info("LINE URL was found:URL=" + lineUrl);

        findUserIdByLineUrlUseCase
                .execute(lineUrl)
                .flatMap(new Func1<LineLinkEntity, Single<UserEntity>>() {
                    @Override
                    public Single<UserEntity> call(LineLinkEntity lineLinkEntity) {
                        return findUserUseCase.execute(lineLinkEntity.key).subscribeOn(Schedulers.io());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {

                    // Notify a notification.
                    UriPendingIntent creator = new UriPendingIntent(getApplicationContext(), Uri.parse(lineUrl));
                    PendingIntent pendingIntent = creator.create();

                    NotificationNotifier notifier = new NotificationNotifier.Builder(getApplicationContext())
                            .intent(pendingIntent)
                            .title(R.string.message_line_user_found)
                            .text(R.string.message_user_using_line, entity.name)
                            .build();
                    notifier.notifyNotification();

                }, e -> LOGGER.error("Failed to notify a LINE notification.", e));
    }
}
