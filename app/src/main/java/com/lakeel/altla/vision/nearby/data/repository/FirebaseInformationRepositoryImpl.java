package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.data.mapper.InformationEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseInformationRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;

public class FirebaseInformationRepositoryImpl implements FirebaseInformationRepository {

    private static final String KEY_POST_TIME = "postTime";

    private DatabaseReference reference;

    private InformationEntityMapper entityMapper = new InformationEntityMapper();

    @Inject
    public FirebaseInformationRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
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

    @Override
    public Observable<InformationEntity> find(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .orderByChild(KEY_POST_TIME)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                InformationEntity entity = snapshot.getValue(InformationEntity.class);
                                subscriber.onNext(entity);
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
}
