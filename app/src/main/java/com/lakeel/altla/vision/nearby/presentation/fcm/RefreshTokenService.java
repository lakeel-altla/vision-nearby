package com.lakeel.altla.vision.nearby.presentation.fcm;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class RefreshTokenService extends FirebaseInstanceIdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);

    private static final String TOKENS_URL = "tokens";

    private DatabaseReference reference;

    @Inject
    public void onCreate() {
        super.onCreate();

        // NOTE:
        // Can not use Dagger2, because custom annotation is not invoked.
        // See https://github.com/google/gcm/issues/197
        reference = FirebaseDatabase.getInstance().getReference(TOKENS_URL);
    }

    @Override
    public void onTokenRefresh() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LOGGER.debug("Refreshed token: " + refreshedToken);

        saveToken(refreshedToken)
                .subscribeOn(Schedulers.io())
                .doOnError(e -> LOGGER.error("Failed token save refreshed token."))
                .subscribe();
    }

    Single<String> saveToken(String token) {
        return Single.create(subscriber -> {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                Task task = reference
                        .child(firebaseUser.getUid())
                        .setValue(token)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(token))
                        .addOnFailureListener(subscriber::onError);

                Exception e = task.getException();
                if (e != null) {
                    throw new DataStoreException(e);
                }
            }
        });
    }
}
