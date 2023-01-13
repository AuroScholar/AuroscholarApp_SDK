package com.auro.application.util.firebase;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.auro.application.core.common.NotificationDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.util.AppLogger;
import com.auro.application.util.NotificationUtils;
import com.auro.application.util.TextUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = FirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        AppLogger.e("onMessageReceived", "From: " + remoteMessage.getData().toString());
        if (remoteMessage == null)
            return;

        //   AppLogger.d("msg", "onMessageReceived: " + convertWithIteration(remoteMessage.getData()));
        try {
            // AppLogger.d("msg", "onMessageReceived: 0" + convertWithIteration(remoteMessage.getData()));
            JSONObject data = new JSONObject(remoteMessage.getData());
            String title = data.optString("title");
            String message = data.optString("message");
            boolean isBackground = data.optBoolean("isBackground");
            String imageUrl = data.optString("imageUrl");
            String timestamp = data.optString("timestamp");
            String navigateto = data.optString("navigateto");

            AppLogger.e(TAG, "title: " + title);
            AppLogger.e(TAG, "message: " + message);
            AppLogger.e(TAG, "isBackground: " + isBackground);
            AppLogger.e(TAG, "navigateto: " + navigateto);
            AppLogger.e(TAG, "imageUrl: " + imageUrl);
            AppLogger.e(TAG, "timestamp: " + timestamp);

            NotificationDataModel notificationDataModel = new NotificationDataModel();
            notificationDataModel.setTitle(title);
            notificationDataModel.setMessage(message);
            notificationDataModel.setIsBackground(isBackground);
            notificationDataModel.setImageUrl(imageUrl);
            notificationDataModel.setNavigateto(navigateto);
            notificationDataModel.setTimestamp(timestamp);

            Intent intent = new Intent(this, SplashScreenAnimationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("message", notificationDataModel);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            if (notificationDataModel != null && !TextUtil.isEmpty(notificationDataModel.getImageUrl())) {
                AppLogger.e("onMessageReceived- 1", notificationDataModel.getImageUrl());
                showNotificationMessageWithBigImage(getApplicationContext(), notificationDataModel.getTitle(), notificationDataModel.getMessage(), notificationDataModel.getTimestamp(), pendingIntent, notificationDataModel.getImageUrl());
            } else {
                showNotificationMessage(getApplicationContext(), notificationDataModel.getTitle(), notificationDataModel.getMessage(), notificationDataModel.getTimestamp(), pendingIntent);
                AppLogger.e("onMessageReceived- 2", notificationDataModel.getImageUrl());
            }
            AppLogger.e("onMessageReceived- 3", notificationDataModel.getImageUrl());
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public String convertWithIteration(Map<String, String> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key) + ", ");
        }
        mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */

    @Override
    public void onNewToken(String token) {
        AppLogger.e("onNewToken", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
        subscribeToTopic();
    }

    private void sendRegistrationToServer(String token) {
        // sending gcm token to server
        AppLogger.e("sendRegistrationToServer", "sendRegistrationToServer: 1//// " + token);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            if (token != null) {
                //prefModel.setDeviceToken(token);
                AuroAppPref.INSTANCE.setPref(prefModel);
                String deviceToken = prefModel.getDeviceToken();
                prefModel.setDeviceToken(deviceToken);
                AppLogger.e("sendRegistrationToServer", "sendRegistrationToServer: 2//// " + deviceToken);
            }





    }

    /**
     * SEND NOTIFICATION TO MULTIPLE DEVICE ON ONE CLICK BY USING SUBCRIBETO TOPIC
     */
    private void subscribeToTopic() {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("auroscholar")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Subscribe to topic";
                            if (!task.isSuccessful()) {
                                msg = "Failed to Subscribe to topic";
                            }
                            AppLogger.v("Device_token", msg);

                        }
                    });
        } catch (Exception e) {

        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, PendingIntent intent) {
        notificationUtils = new NotificationUtils(context);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, PendingIntent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}