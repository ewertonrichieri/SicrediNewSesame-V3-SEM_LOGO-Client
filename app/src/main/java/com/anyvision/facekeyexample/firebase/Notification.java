package com.anyvision.facekeyexample.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.activities.logged.SolicitationExtensionActivity;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;


public class Notification extends FirebaseMessagingService {
    private Authentication auth;

    public void requestTokenFirebaseNotification() {

        //Para reativar o FCM, faça uma chamada de tempo de execução:
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("token", token);
                        Firebase.getInstance().sendTokenFirebaseToServer(token);
                        String msg = getString(R.string.fcm_token, token);
                        Log.d("token", msg);

                    }
                });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            Log.d("TESTE_FIREBASE", "entrou na mensagem");

            String messageBody = "";
            String title = "";

            if (remoteMessage.getNotification() != null) {
                Log.d("teste", remoteMessage.getNotification().getTitle());
                Log.d("teste", remoteMessage.getNotification().getBody());

                title = remoteMessage.getNotification().getTitle();
                messageBody = remoteMessage.getNotification().getBody();
            }

            if (remoteMessage.getData().size() > 0) {
                messageBody = remoteMessage.getData().get("mensagem");
                title = remoteMessage.getData().get("titulo");
            }

            if (remoteMessage.getFrom().contains("%")) {
                String macTopicSend = "%" + InfoMobile.getMacAddress().replace(":", "");
                String macTopicReceived = remoteMessage.getFrom().replace("/topics/", "");


                if (macTopicSend == macTopicReceived)
                    Log.d("jonejaf_token", "DEU CERTO" + remoteMessage.getFrom());
                LoginActivity.startThreadLogin();
            }

            if(SolicitationExtensionActivity.onActive()){
                auth = new Authentication(GetVariables.getInstance().getServerUrl());
                auth.requestToken("aprovaReprovaExtesao", "FirebaseNotification");
            }

            // ...
            showNotification(title, messageBody);

            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d("notification_from", "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d("notification_data", "Message data payload: " + remoteMessage.getData());

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use WorkManager.
                    Log.d("notification", "Check if data needs to be processed by long running job");
                    //scheduleJob();
                } else {
                    // Handle message within 10 seconds
                    Log.d("notification", "Check if data needs to be processed by long running job");
                    //handleNow();
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d("notification", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showNotification(String title, String messageBody) {
        try {
            int id = (int) (System.currentTimeMillis() / 1000);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logo_fk_2015)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sendNotification() {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder("cOFJ_De2w9Q:APA91bEe5QtOnRzfxG4Mbux-QuJcXFd20KI3xqWFeBGx87cyNgXwivEynrgwnadglylhHO11znMRrvW5RhFFeEhmyatsG09-i2drdGST9w5d6czSS4mX9Nkrzo7kZoDjY08I9nOeg21x@fcm.googleapis.com")
//        fm.send(new RemoteMessage.Builder( "eiPpA6nIaWE:APA91bEQKpuIlbv7Uhhg_Uv92KYkQ-sqLt3trtRmguzUSLBWvi9UMXrnUpN6R1ReWHbOiujCl3O8kWrkafUo4zJ15GuDEYJKePQGWUoIhnU1rBW2H1KhzvKAmEbsuLxSHgVHtLNP-gkY")

                .setMessageId(Integer.toString(1))
                .addData("my_message", "Hello World")
                .addData("my_action", "SAY_HELLO")
                .build());
    }

    //somente a primeira vez que esse metodo é chamado
    @Override
    public void onNewToken(String token) {
        String mac = InfoMobile.getMacAddress().replace(":", "");
        FirebaseMessaging.getInstance().subscribeToTopic("%" + mac);
        Firebase.unRegister(mac, false);
        super.onNewToken(token);
    }

    public static void RescueToken() {
        final String autorizacao = "1071532943381";
        final String firebase = "FCM";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId(); // remove token
                    String token = FirebaseInstanceId.getInstance().getToken(autorizacao, firebase);
                } catch (IOException e) {
                }
            }
        }).start();
    }
}
