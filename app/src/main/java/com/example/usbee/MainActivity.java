package com.example.usbee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.notifications.pushnotifications.NotificationPayload;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("JavascriptInterface")
    class JavaScriptInterface {
        @JavascriptInterface
        public void callFromWeb(String str) {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Data from JavaScript: " + str, Toast.LENGTH_SHORT).show());
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationPayload payload = NotificationPayload.fromIntent(getIntent());

        if (payload != null) {
            // Record notification opened when activity launches
            Amplify.Notifications.Push.recordNotificationOpened(payload,
                    () -> Log.i("MyAmplifyApp", "Successfully recorded notification opened"),
                    error -> Log.e("MyAmplifyApp", "Error recording notification opened", error)
            );
        }
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.usBeeWebView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        String urlString = "https://948a-180-232-183-242.ngrok-free.app/user";
        webView.loadUrl(urlString);
        webView.addJavascriptInterface(new JavaScriptInterface(), "NativeInterface");
    }

}
