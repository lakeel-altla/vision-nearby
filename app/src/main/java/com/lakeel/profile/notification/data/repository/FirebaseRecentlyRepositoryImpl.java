package com.lakeel.profile.notification.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.data.entity.RecentlyEntity;
import com.lakeel.profile.notification.data.execption.DataStoreException;
import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseRecentlyRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.Subscriber;

public class FirebaseRecentlyRepositoryImpl implements FirebaseRecentlyRepository {

    private static final String ID_KEY = "id";

    private static final String TIMESTAMP_KEY = "passingTime";

    private static final String LOCATION_KEY = "location";

    private static final String TEXT_KEY = "text";

    private DatabaseReference mReference;

    @Inject
    FirebaseItemsRepository mFirebaseItemsRepository;

    @Inject
    public FirebaseRecentlyRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Observable<RecentlyEntity> findRecently() {
        return Observable.create(new Observable.OnSubscribe<RecentlyEntity>() {

            @Override
            public void call(Subscriber<? super RecentlyEntity> subscriber) {
                mReference
                        .child(MyUser.getUid())
                        .orderByChild(TIMESTAMP_KEY)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    RecentlyEntity entity = snapshot.getValue(RecentlyEntity.class);
                                    entity.key = snapshot.getKey();
                                    subscriber.onNext(entity);
                                }
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        });
            }
        });
    }

    @Override
    public Single<RecentlyEntity> findRecentlyByKey(String key) {
        return Single.create(subscriber -> {
            mReference.child(MyUser.getUid()).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    RecentlyEntity data = snapshot.getValue(RecentlyEntity.class);
                    data.key = snapshot.getKey();
                    subscriber.onSuccess(data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
        });
    }

    @Override
    public Single<Long> findTimes(String id) {
        return Single.create(subscriber ->
                mReference
                        .child(MyUser.getUid())
                        .orderByChild(ID_KEY)
                        .equalTo(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(dataSnapshot.getChildrenCount());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<String> saveLocationText(String key, String language, String locationText) {
        return Single.create(subscriber -> {
            HashMap<String, Object> value = new HashMap<>();
            value.put(language, locationText);

            Task task = mReference
                    .child(MyUser.getUid())
                    .child(key)
                    .child(LOCATION_KEY)
                    .child(TEXT_KEY)
                    .updateChildren(value)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(locationText))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }
}
