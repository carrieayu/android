package com.example.usbee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.notifications.pushnotifications.NotificationContentProvider;
import com.amplifyframework.notifications.pushnotifications.NotificationPayload;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onNewToken(String token){
        super.onNewToken(token);
        // Register device with Pinpoint
        Amplify.Notifications.Push.registerDevice(token,
                () -> Log.i("MyAmplifyApp", "Successfully registered device"),
                error -> Log.e("MyAmplifyApp", "Error registering device", error)
        );
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Convert the RemoteMessage into a NotificationPayload
        NotificationPayload notificationPayload = NotificationPayload.builder(
                new NotificationContentProvider.FCM(remoteMessage.getData())
        ).build();

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("pinpoint.notification.title");
        String defaultTitle = data.get("default");
        String body = data.get("pinpoint.notification.body");
        String payloadData = data.get("your_custom_key");
        if(!defaultTitle.isEmpty() || !defaultTitle.equals(null)){
            title = defaultTitle;
        }
        MyNotificationJobService.showNotification(this, title,body,payloadData);
//        MyNotificationJobService.showPopupNotification(this, title, body);

        // Amplify should handle notification if it is sent from Pinpoint
        boolean isAmplifyMessage = Amplify.Notifications.Push.shouldHandleNotification(notificationPayload);
        if (isAmplifyMessage) {
            // Record notification received with Amplify
            Amplify.Notifications.Push.recordNotificationReceived(notificationPayload,
                    () -> Log.i("MyAmplifyApp", "Successfully recorded a notification received"),
                    error -> Log.e("MyAmplifyApp", "Error recording notification received", error)
            );
        }
    }
}
