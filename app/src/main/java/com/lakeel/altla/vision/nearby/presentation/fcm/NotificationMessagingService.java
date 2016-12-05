package com.lakeel.altla.vision.nearby.presentation.fcm;

import android.app.PendingIntent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lakeel.altla.vision.nearby.presentation.intent.DefaultPendingIntent;
import com.lakeel.altla.vision.nearby.presentation.view.notification.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationMessagingService extends FirebaseMessagingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessagingService.class);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification == null) {
            LOGGER.error("Notification payload was empty.");
            return;
        }

        String title = notification.getTitle();
        String message = notification.getBody();

        LOGGER.debug("Notification message is " + message);

        DefaultPendingIntent defaultPendingIntent = new DefaultPendingIntent(getApplicationContext());
        PendingIntent pendingIntent = defaultPendingIntent.create();

        Notification notifier = new Notification.Builder(getApplicationContext())
                .title(title)
                .text(message)
                .intent(pendingIntent)
                .build();
        notifier.notifyNotification();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        LOGGER.debug("This message has been deleted.");
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        super.onSendError(msgId, e);
        LOGGER.error("FCM has failed to send a notification message.", e);
    }
}
