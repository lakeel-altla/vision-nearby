package com.lakeel.profile.notification.presentation.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.data.entity.LINELinksEntity;
import com.lakeel.profile.notification.presentation.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.Iterator;
import java.util.UUID;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LINEService extends IntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LINEService.class);

    private static final String ITEMS_REFERENCE = "items";

    private static final String LINE_REFERENCE = "links/line";

    private static final String URL_KEY = "url";

    public LINEService() {
        super(LINEService.class.getSimpleName());
    }

    public LINEService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String lineUrl = intent.getStringExtra(IntentKey.LINE_URL.name());

        LOGGER.info("Line URL was found:LINE URL = " + lineUrl);

        findUserIdByLINEUrl(lineUrl)
                .flatMap(new Func1<LINELinksEntity, Single<ItemsEntity>>() {
                    @Override
                    public Single<ItemsEntity> call(LINELinksEntity lineLinksEntity) {
                        return findItemById(lineLinksEntity.key).subscribeOn(Schedulers.io());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    notificationIntent.setData(Uri.parse(lineUrl));

                    Context context = getApplicationContext();

                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                    int uuid = UUID.randomUUID().hashCode();

                    Notification notification = new NotificationCompat.Builder(context)
                            .setContentTitle(context.getResources().getString(R.string.message_line_user_found, entity.name))
                            .setTicker(lineUrl)
                            .setContentText(lineUrl)
                            .setSmallIcon(R.mipmap.ic_nearby_white)
                            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .build();

                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(uuid, notification);
                }, e -> LOGGER.error("Failed to notify LINE notification.", e));
    }

    Single<LINELinksEntity> findUserIdByLINEUrl(String url) {
        return Single.create(new Single.OnSubscribe<LINELinksEntity>() {
            @Override
            public void call(SingleSubscriber<? super LINELinksEntity> subscriber) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(LINE_REFERENCE);
                reference.orderByChild(URL_KEY).equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = iterable.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot snapshot = iterator.next();
                            LINELinksEntity entity = snapshot.getValue(LINELinksEntity.class);
                            entity.key = snapshot.getKey();
                            subscriber.onSuccess(entity);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    Single<ItemsEntity> findItemById(String id) {
        return Single.create(new Single.OnSubscribe<ItemsEntity>() {
            @Override
            public void call(SingleSubscriber<? super ItemsEntity> subscriber) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ITEMS_REFERENCE);
                reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ItemsEntity entity = dataSnapshot.getValue(ItemsEntity.class);
                        subscriber.onSuccess(entity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            }
        });
    }
}
