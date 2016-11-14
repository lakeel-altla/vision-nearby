package com.lakeel.profile.notification.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.data.entity.FavoritesEntity;
import com.lakeel.profile.notification.data.execption.DataStoreException;
import com.lakeel.profile.notification.domain.repository.FirebaseFavoriteRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class FirebaseFavoriteRepositoryImpl implements FirebaseFavoriteRepository {

    private DatabaseReference mReference;

    @Inject
    public FirebaseFavoriteRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<FavoritesEntity> findFavorites() {

        return Observable.create(subscriber -> {
            mReference
                    .child(MyUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                FavoritesEntity entity = snapshot.getValue(FavoritesEntity.class);
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
    public Single<FavoritesEntity> findFavoriteById(String id) {
        return Single.create(subscriber ->
                mReference
                        .child(MyUser.getUid())
                        .child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FavoritesEntity entity = dataSnapshot.getValue(FavoritesEntity.class);
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<FavoritesEntity> saveFavorite(String id) {
        return Single.create(subscriber -> {
            FavoritesEntity entity = new FavoritesEntity();

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
