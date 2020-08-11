package com.anyvision.facekeyexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    private String topic;


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        try {
//            Toast.makeText(context, "received dei certo!!!", Toast.LENGTH_LONG).show();

            Log.d("GcmBReceiver", action);

            if (intent.getStringExtra("from") != null)
                topic = intent.getStringExtra("from");

            if (topic.contains("%")) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("enableBtnRegister", true);
                editor.commit();
                Log.d("GcmBReceiver", "GRAVOU NO SHARED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "DEFEITO BroadCast", Toast.LENGTH_LONG).show();
            Log.d("GcmBReceiver", "Deu ruim aqui");

        }
    }
}


