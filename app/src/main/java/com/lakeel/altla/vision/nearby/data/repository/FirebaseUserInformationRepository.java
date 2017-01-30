package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.data.mapper.entity.InformationEntityMapper;
import com.lakeel.altla.vision.nearby.data.mapper.model.InformationMapper;
import com.lakeel.altla.vision.nearby.domain.model.Information;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class FirebaseUserInformationRepository {

    private final InformationEntityMapper entityMapper = new InformationEntityMapper();

    private final InformationMapper informationMapper = new InformationMapper();

    private final DatabaseReference reference;

    @Inject
    public FirebaseUserInformationRepository(@Named("userInformationUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Completable save(String userId, String title, String message) {
        return Completable.create(subscriber -> {
            InformationEntity entity = entityMapper.map(title, message);
            Map<String, Object> map = entity.toMap();

            Task task = reference
                    .child(userId)
                    .push()
                    .setValue(map);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

    public Observable<Information> findInformationList(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                InformationEntity entity = snapshot.getValue(InformationEntity.class);
                                subscriber.onNext(informationMapper.map(entity, snapshot.getKey()));
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

    public Single<Information> findInformation(String userId, String informationId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(informationId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                InformationEntity entity = snapshot.getValue(InformationEntity.class);
                                subscriber.onSuccess(informationMapper.map(entity, snapshot.getKey()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }
}
