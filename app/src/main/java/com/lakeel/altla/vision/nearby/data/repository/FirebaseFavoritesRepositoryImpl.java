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
                                Boolean isFavorite = (Boolean) snapshot.getValue();
                                if (isFavorite == null || !isFavorite) {
                                    subscriber.onNext(null);
                                } else {
                                    FavoriteEntity entity = new FavoriteEntity();
                                    entity.userId = snapshot.getKey();
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
                                Boolean isFavorite = (Boolean) dataSnapshot.getValue();
                                if (isFavorite == null || !isFavorite) {
                                    subscriber.onSuccess(null);
                                } else {
                                    FavoriteEntity entity = new FavoriteEntity();
                                    entity.userId = dataSnapshot.getKey();
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
                    .setValue(true);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    @Override
    public Completable removeFavorite(String userId, String otherUserId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(otherUserId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }
}
