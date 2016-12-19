package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class FirebaseFavoritesRepositoryImpl implements FirebaseFavoritesRepository {

    private DatabaseReference reference;

    @Inject
    public FirebaseFavoritesRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Observable<FavoriteEntity> findFavoritesByUserId(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Boolean bool = (Boolean) snapshot.getValue();
                                if (bool == null) {
                                    subscriber.onNext(null);
                                } else {
                                    FavoriteEntity entity = new FavoriteEntity();
                                    entity.favoriteUserId = snapshot.getKey();
                                    subscriber.onNext(entity);
                                }
                            }
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    @Override
    public Single<FavoriteEntity> findFavorite(String userId, String otherUserId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(otherUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Boolean bool = (Boolean) dataSnapshot.getValue();
                                if (bool == null) {
                                    subscriber.onSuccess(null);
                                } else {
                                    FavoriteEntity entity = new FavoriteEntity();
                                    entity.favoriteUserId = dataSnapshot.getKey();
                                    subscriber.onSuccess(entity);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Completable saveFavorite(String myUserId, String otherUserId) {
        return Completable.create(subscriber -> {
            Task<Void> task = reference
                    .child(myUserId)
                    .child(otherUserId)
                    .setValue(true)
                    .addOnSuccessListener(aVoid -> subscriber.onCompleted())
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
    public Completable removeFavorite(String userId, String otherUserId) {
        return Completable.create(subscriber ->
                reference
                        .child(userId)
                        .child(otherUserId)
                        .removeValue((databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }
}
