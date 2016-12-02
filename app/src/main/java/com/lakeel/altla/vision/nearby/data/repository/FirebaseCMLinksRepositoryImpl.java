package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public final class FirebaseCMLinksRepositoryImpl implements FirebaseCMLinksRepository {

    private static final String KEY_API_KEY = "apiKey";

    private static final String KEY_SECRET_KEY = "secretKey";

    private static final String KEY_JID_KEY = "jidKey";

    private DatabaseReference mReference;

    @Inject
    public FirebaseCMLinksRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> findCmJidByItemId(String itemId) {
        return Single.create(subscriber -> mReference.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CMLinkEntity entity = dataSnapshot.getValue(CMLinkEntity.class);
                subscriber.onSuccess(entity.jid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        }));
    }

    @Override
    public Single<CMLinkEntity> findCmLinks() {
        return Single.create(subscriber ->
                mReference.child(MyUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CMLinkEntity entity = dataSnapshot.getValue(CMLinkEntity.class);
                        subscriber.onSuccess(entity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                }));
    }

    @Override
    public Single<String> saveCMApiKey(String apiKey) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Task<Void> task = mReference
                        .child(MyUser.getUid())
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
    public Single<String> saveCMSecretKey(String secretKey) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Task<Void> task = mReference
                        .child(MyUser.getUid())
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
    public Single<String> saveCMJid(String jid) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Task<Void> task = mReference
                        .child(MyUser.getUid())
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
