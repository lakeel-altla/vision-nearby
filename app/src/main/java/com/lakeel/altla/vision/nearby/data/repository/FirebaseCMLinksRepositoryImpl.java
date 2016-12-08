package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCmLinksRepository;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public final class FirebaseCmLinksRepositoryImpl implements FirebaseCmLinksRepository {

    private static final String KEY_API_KEY = "apiKey";

    private static final String KEY_SECRET_KEY = "secretKey";

    private static final String KEY_JID_KEY = "jidKey";

    private DatabaseReference reference;

    @Inject
    public FirebaseCmLinksRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> findJidByUserId(String userId) {
        return Single.create(subscriber -> reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CmLinkEntity entity = dataSnapshot.getValue(CmLinkEntity.class);
                subscriber.onSuccess(entity.jid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        }));
    }

    @Override
    public Single<CmLinkEntity> findCmLinksByUserId(String userId) {
        return Single.create(subscriber ->
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CmLinkEntity entity = dataSnapshot.getValue(CmLinkEntity.class);
                        subscriber.onSuccess(entity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                }));
    }

    @Override
    public Single<String> saveApiKey(String userId, String apiKey) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Task<Void> task = reference
                        .child(userId)
                        .child(KEY_API_KEY)
                        .setValue(apiKey)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(apiKey))
                        .addOnFailureListener(subscriber::onError);

                Exception exception = task.getException();
                if (exception != null) {
                    throw new DataStoreException(exception);
                }
            }
        });
    }

    @Override
    public Single<String> saveSecretKey(String userId, String secretKey) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Task<Void> task = reference
                        .child(userId)
                        .child(KEY_SECRET_KEY)
                        .setValue(secretKey)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(secretKey))
                        .addOnFailureListener(subscriber::onError);

                Exception exception = task.getException();
                if (exception != null) {
                    throw new DataStoreException(exception);
                }
            }
        });
    }

    @Override
    public Single<String> saveJid(String userId, String jid) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Task<Void> task = reference
                        .child(userId)
                        .child(KEY_JID_KEY)
                        .setValue(jid)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(jid))
                        .addOnFailureListener(subscriber::onError);

                Exception exception = task.getException();
                if (exception != null) {
                    throw new DataStoreException(exception);
                }
            }
        });
    }
}
