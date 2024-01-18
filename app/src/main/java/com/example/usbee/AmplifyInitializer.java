package com.example.usbee;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.pushnotifications.pinpoint.AWSPinpointPushNotificationsPlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class AmplifyInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
            Log.i("MyAmplifyApp", "Initialized Firebase");
        }
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSPinpointPushNotificationsPlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSPinpointPushNotificationsPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }
}
