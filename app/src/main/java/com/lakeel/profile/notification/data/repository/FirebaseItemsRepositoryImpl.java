package com.lakeel.profile.notification.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.data.MyUser;
import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.data.execption.DataStoreException;
import com.lakeel.profile.notification.data.mapper.ItemsEntityMapper;
import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public final class FirebaseItemsRepositoryImpl implements FirebaseItemsRepository {

    private static final String KEY_NAME = "name";

    private DatabaseReference mReference;

    private ItemsEntityMapper mItemsEntityMapper = new ItemsEntityMapper();

    @Inject
    public FirebaseItemsRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<ItemsEntity> saveItem() {
        return Single.create(new Single.OnSubscribe<ItemsEntity>() {
            @Override
            public void call(SingleSubscriber<? super ItemsEntity> subscriber) {
                ItemsEntity entity = mItemsEntityMapper.map();
                Task task = mReference.child(MyUser.getUid()).setValue(entity)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                        .addOnFailureListener(subscriber::onError);

                Exception e = task.getException();
                if (e != null) {
                    throw new DataStoreException(e);
                }
            }
        });
    }

    @Override
    public Single<ItemsEntity> findItemsById(String id) {

        return Single.create(new Single.OnSubscribe<ItemsEntity>() {

            @Override
            public void call(SingleSubscriber<? super ItemsEntity> subscriber) {
                mReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

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
