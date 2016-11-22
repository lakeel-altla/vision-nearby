package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.data.entity.LINELinksEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;


public class FirebaseLINELinksRepositoryImpl implements FirebaseLINELinksRepository {

    private DatabaseReference mReference;

    @Inject
    public FirebaseLINELinksRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveUrl(String url) {
        return Single.create(subscriber -> {
            LINELinksEntity entity = new LINELinksEntity();
            entity.url = url;

            Task<Void> task = mReference
                    .child(MyUser.getUid())
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
    public Single<LINELinksEntity> findByUserId(String userId) {
        return Single.create(subscriber ->
                mReference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                LINELinksEntity entity = dataSnapshot.getValue(LINELinksEntity.class);
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }
}
