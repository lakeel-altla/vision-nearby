package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLineLinksRepository;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;


public class FirebaseLineLinksRepositoryImpl implements FirebaseLineLinksRepository {

    private static final String URL_KEY = "url";

    private DatabaseReference reference;

    @Inject
    public FirebaseLineLinksRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveUrl(String userId, String url) {
        return Single.create(subscriber -> {
            LineLinkEntity entity = new LineLinkEntity();
            entity.url = url;

            Task<Void> task = reference
                    .child(userId)
                    .setValue(entity)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(url))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
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

    @Override
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
