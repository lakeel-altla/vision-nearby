package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class UserFavoriteRepository {

    private final DatabaseReference reference;

    @Inject
    public UserFavoriteRepository(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Observable<String> findAll(@NonNull String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Boolean isFavorite = (Boolean) snapshot.getValue();

                                if (isFavorite == null || !isFavorite) {
                                    throw new DataStoreException("Invalid value:isFavorite=" + isFavorite);
                                }

                                // Return favorite user id.
                                subscriber.onNext(snapshot.getKey());
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

    public Single<Favorite> find(@NonNull String userId, @NonNull String favoriteUserId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(favoriteUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(map(userId, dataSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Completable save(@NonNull Favorite favorite) {
        return Completable.create(subscriber -> {
            Task<Void> task = reference
                    .child(favorite.userId)
                    .child(favorite.favoriteUserId)
                    .setValue(true);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable remove(@NonNull String userId, @NonNull String favoriteUserId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(favoriteUserId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

    @Nullable
    private Favorite map(String userId, DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() == null) {
            return null;
        }

        Boolean isFavoriteUser = (Boolean) dataSnapshot.getValue();
        if (!isFavoriteUser) {
            return null;
        }

        Favorite favorite = new Favorite();
        favorite.favoriteUserId = dataSnapshot.getKey();
        favorite.userId = userId;
        return favorite;
    }
}