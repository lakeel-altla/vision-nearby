package com.lakeel.profile.notification.data.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.data.entity.LocationsDataEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseLocationsDataRepository;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Single;

public class FirebaseLocationsDataRepositoryImpl implements FirebaseLocationsDataRepository {

    private static final String KEY_ID = "id";

    private DatabaseReference mReference;

    @Inject
    public FirebaseLocationsDataRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<LocationsDataEntity> findLocationsDataById(String id) {
        return Single.create(subscriber ->
                mReference
                        .orderByChild(KEY_ID)
                        .equalTo(id)
                        .limitToFirst(1)
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
                                    LocationsDataEntity entity = snapshot.getValue(LocationsDataEntity.class);
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
