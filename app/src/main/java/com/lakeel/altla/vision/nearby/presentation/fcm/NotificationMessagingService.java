package com.lakeel.altla.vision.nearby.presentation.fcm;

import android.app.PendingIntent;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class NotificationMessagingService extends FirebaseMessagingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessagingService.class);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification payload = remoteMessage.getNotification();
        if (payload == null) {
            LOGGER.warn("Notification payload was empty.");
            return;
        }

        String title = payload.getTitle();
        String message = payload.getBody();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        LocalNotification localNotification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
        localNotification.show();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        LOGGER.info("Payload body has been deleted.");
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        super.onSendError(msgId, e);
        LOGGER.error("FCM has failed token send a notification body.", e);
    }
}
