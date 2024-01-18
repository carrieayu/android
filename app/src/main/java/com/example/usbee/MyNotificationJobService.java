package com.example.usbee;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyNotificationJobService extends Service {
    public static final String CHANNEL_ID = "MyNotificationJobService";
    public static final String CHANNEL_NAME = "My Notification Channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("MissingPermission")
    public static void showNotification(Context context, String title, String content, String payloadData) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("notification_payload", payloadData);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE  // Add FLAG_IMMUTABLE
        );


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background))
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setFullScreenIntent(null, true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
    @SuppressLint("MissingPermission")
    public static void showPopupNotification(Context context, String title, String content) {
        createNotificationChannel(context);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // Check if the context is still valid
                if (isValidContextForDialog(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(title)
                            .setMessage(content)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Handle the positive button click
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Handle the negative button click
                                    dialog.dismiss();
                                }
                            });

                    // Create and show the AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    System.out.println("Ning Gawas");
                }
            }
        });
    }

    private static boolean isValidContextForDialog(Context context) {
        if (context == null) {
            Log.e("PopupNotification", "Context is null");
            return false;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isFinishing() || activity.isDestroyed()) {
                    Log.e("PopupNotification", "Activity is finishing or destroyed");
                    return false;
                }
            } else {
                if (activity.isFinishing()) {
                    Log.e("PopupNotification", "Activity is finishing");
                    return false;
                }
            }
        }

        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
