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
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class FirebaseFavoritesRepositoryImpl implements FirebaseFavoritesRepository {

    private DatabaseReference mReference;

    @Inject
    public FirebaseFavoritesRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<FavoriteEntity> findFavorites() {

        return Observable.create(subscriber -> {
            mReference
                    .child(MyUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                FavoriteEntity entity = snapshot.getValue(FavoriteEntity.class);
                                entity.key = snapshot.getKey();
                                subscriber.onNext(entity);
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
    public Single<FavoriteEntity> findFavoriteById(String id) {
        return Single.create(subscriber ->
                mReference
                        .child(MyUser.getUid())
                        .child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FavoriteEntity entity = dataSnapshot.getValue(FavoriteEntity.class);
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<FavoriteEntity> saveFavorite(String id) {
        return Single.create(subscriber -> {
            FavoriteEntity entity = new FavoriteEntity();

            Task<Void> task = mReference.child(MyUser.getUid()).child(id).setValue(entity.toMap())
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
    public Completable removeFavoriteByUid(String id) {
        return Completable.create(subscriber -> {
            mReference.child(MyUser.getUid()).child(id)
                    .removeValue((databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }
}
