package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;

public final class FirebaseTokensRepositoryImpl implements FirebaseTokensRepository {

    private DatabaseReference reference;

    @Inject
    public FirebaseTokensRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveToken(String token) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(MyUser.getUid())
                    .setValue(token)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(token))
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }
        });
    }
}
