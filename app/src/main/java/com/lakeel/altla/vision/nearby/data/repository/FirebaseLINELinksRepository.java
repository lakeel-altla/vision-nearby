package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.entity.LineLinkEntityMapper;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;


public class FirebaseLINELinksRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/link/line";

    private static final String URL_KEY = "url";

    private final LineLinkEntityMapper entityMapper = new LineLinkEntityMapper();

    private final DatabaseReference reference;

    @Inject
    public FirebaseLINELinksRepository() {
        this.reference = FirebaseDatabase.getInstance().getReference(DATABASE_URI);
    }

    public Single<String> saveLineUrl(String userId, String url) {
        return Single.create(subscriber -> {
            LineLinkEntity entity = entityMapper.map(url);

            Task<Void> task = reference
                    .child(userId)
                    .setValue(entity);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(url);
        });
    }

    public Single<LineLinkEntity> findByUserId(String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                LineLinkEntity entity = dataSnapshot.getValue(LineLinkEntity.class);
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<LineLinkEntity> findUserIdByLineUrl(String url) {
        return Single.create(new Single.OnSubscribe<LineLinkEntity>() {
            @Override
            public void call(SingleSubscriber<? super LineLinkEntity> subscriber) {
                reference.orderByChild(URL_KEY).equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = iterable.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot snapshot = iterator.next();
                            LineLinkEntity entity = snapshot.getValue(LineLinkEntity.class);
                            entity.userId = snapshot.getKey();
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
}
