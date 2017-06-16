package com.lakeel.altla.vision.nearby.presentation.fcm;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RefreshTokenService extends FirebaseInstanceIdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userDeviceTokens";

    private DatabaseReference reference;

    @Inject
    public void onCreate() {
        super.onCreate();

        // NOTE:
        // Can not use Dagger2, because custom annotation is not invoked.
        // See https://github.com/google/gcm/issues/197
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    @Override
    public void onTokenRefresh() {
        if (!CurrentUser.isSignedIn()) {
            return;
        }

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LOGGER.debug("Refreshed token=" + refreshedToken);

        saveToken(refreshedToken)
                .subscribeOn(Schedulers.io())
                .subscribe(e -> LOGGER.error("Failed to save refreshed token.", e),
                        () -> {
                        });
    }

    private Completable saveToken(String token) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(CurrentUser.getUid())
                    .setValue(token);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onCompleted();
        });
    }
}
