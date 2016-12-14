package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.TokenEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.Subscriber;

public final class FirebaseTokensRepositoryImpl implements FirebaseTokensRepository {

    private DatabaseReference reference;

    @Inject
    public FirebaseTokensRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveToken(String userId, String beaconId, String token) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(beaconId)
                    .setValue(token)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(token))
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }
        });
    }

    @Override
    public Single<String> findTokenByUserId(String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String token = (String) dataSnapshot.getValue();
                                subscriber.onSuccess(token);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Observable<TokenEntity> findTokensByUserId(String userId) {
        return Observable.create(new Observable.OnSubscribe<TokenEntity>() {
            @Override
            public void call(Subscriber<? super TokenEntity> subscriber) {
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    TokenEntity entity = new TokenEntity();
                                    entity.beaconId = snapshot.getKey();
                                    entity.token = (String) snapshot.getValue();;
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
}
