package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.entity.FavoriteEntity;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class FirebaseFavoritesRepository {

    private DatabaseReference reference;

    @Inject
    public FirebaseFavoritesRepository(@Named("favoritesUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Observable<FavoriteEntity> findFavoritesByUserId(String myUserId) {
        return Observable.create(subscriber -> {
            reference
                    .child(myUserId)
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

    public Single<FavoriteEntity> findFavorite(String myUserId, String favoriteUserId) {
        return Single.create(subscriber ->
                reference
                        .child(myUserId)
                        .child(favoriteUserId)
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

    public Completable saveFavorite(String myUserId, String favoriteUserId) {
        return Completable.create(subscriber -> {
            Task<Void> task = reference
                    .child(myUserId)
                    .child(favoriteUserId)
                    .setValue(true);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable removeFavorite(String myUserId, String favoriteUserId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(myUserId)
                    .child(favoriteUserId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }
}
