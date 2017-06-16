package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.domain.model.Information;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class UserInformationRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userInformation";

    private final DatabaseReference reference;

    @Inject
    UserInformationRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Completable save(@NonNull Information information) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(information.userId)
                    .push()
                    .setValue(information);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

    public Observable<Information> findAll(@NonNull String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                subscriber.onNext(map(userId, snapshot));
                            }
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    public Single<Information> find(@NonNull String userId, @NonNull String informationId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(informationId)
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

    private Information map(String userId, DataSnapshot dataSnapshot) {
        Information information = dataSnapshot.getValue(Information.class);
        information.userId = userId;
        information.informationId = dataSnapshot.getKey();
        return information;
    }
}