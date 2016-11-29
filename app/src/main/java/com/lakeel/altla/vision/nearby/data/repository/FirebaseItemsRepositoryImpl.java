package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.ItemsEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.SingleSubscriber;

public final class FirebaseItemsRepositoryImpl implements FirebaseItemsRepository {

    private static final String KEY_NAME = "name";

    private static final String KEY_BEACONS = "beacons";

    private static final String KEY_LAST_USED_TIME = "lastUsedTime";

    private DatabaseReference mReference;

    private ItemsEntityMapper mItemsEntityMapper = new ItemsEntityMapper();

    @Inject
    public FirebaseItemsRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Completable saveItem() {
        return Completable.create(subscriber -> {
            Map<String, Object> map = mItemsEntityMapper.map();
            Task task = mReference.child(MyUser.getUid()).updateChildren(map)
                    .addOnSuccessListener(aVoid -> subscriber.onCompleted())
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }
        });
    }

    @Override
    public Single<ItemsEntity> findItemsById(String id) {
        return Single.create(new Single.OnSubscribe<ItemsEntity>() {

            @Override
            public void call(SingleSubscriber<? super ItemsEntity> subscriber) {
                mReference
                        .child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ItemsEntity entity = dataSnapshot.getValue(ItemsEntity.class);
                                entity.key = dataSnapshot.getKey();
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        });
            }
        });
    }

    @Override
    public Single<ItemsEntity> findItemsByName(String name) {
        // TODO: Use Elastic Search.
        return Single.create(subscriber ->
                mReference
                        .orderByChild(KEY_NAME)
                        .equalTo(name)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChildren()) {
                                    subscriber.onSuccess(null);
                                    return;
                                }

                                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                                Iterator<DataSnapshot> iterator = iterable.iterator();
                                while (iterator.hasNext()) {
                                    DataSnapshot snapshot = iterator.next();
                                    ItemsEntity entity = snapshot.getValue(ItemsEntity.class);
                                    entity.key = snapshot.getKey();
                                    subscriber.onSuccess(entity);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }
}
