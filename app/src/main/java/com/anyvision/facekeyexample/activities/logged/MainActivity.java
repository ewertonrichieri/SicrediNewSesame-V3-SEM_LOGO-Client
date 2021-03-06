package com.anyvision.facekeyexample.activities.logged;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.MessageTopic;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.facekeyexample.utils.Enum;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Authentication auth;
    private Button btnTimeExtend;
    private Button btnTimeExtend2;
    private Button btnArmarAlarmes;
    private Button btnLigarDesligarLuzes;
    private Button btnChamado;
    private Button btnPanico;
    private String typeAccount;
    private String nameAgencia;
    private static String aprovaReprovaExtesao = "aprovaReprovaExtesao";
    private static String chamadoDescriptionsButtons = "chamadoDescriptionsButtons";
    private static Activity finishMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeAccount = GetVariables.getInstance().getSpTypeAccount();

        if(typeAccount == null)
            GetVariables.getInstance().setSpTypeAccount("AGENCIA");

        SharedPreferences sharedTypeAccount = getSharedPreferences("typeAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedTypeAccount.edit();
        editor.clear().commit();
        editor.putString("typeAccount", typeAccount);
        editor.apply();

        Log.d("chamadoLista", aprovaReprovaExtesao + " " + chamadoDescriptionsButtons);

        nameAgencia = GetVariables.getInstance().getNameAgencia();
        finishMainActivity = this;

        auth = new Authentication(GetVariables.getInstance().getServerUrl());
        btnTimeExtend = findViewById(R.id.btnExtend);
        btnTimeExtend2 = findViewById(R.id.btnExtend2);
        btnArmarAlarmes = findViewById(R.id.btnArmar);
        btnLigarDesligarLuzes = findViewById(R.id.btnLigarDesligar);
        btnChamado = findViewById(R.id.btnChamado);
        btnPanico = findViewById(R.id.btnPanico);

        auth.requestToken(aprovaReprovaExtesao, chamadoDescriptionsButtons);

        if(nameAgencia == null)
                nameAgencia = "App.AGENCIA.POC.AGENCIA0001";

            FirebaseMessaging.getInstance().unsubscribeFromTopic("REGIONAL");
            FirebaseMessaging.getInstance().subscribeToTopic("AGENCIA");

            SharedPreferences prefDescriptions = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int size = prefDescriptions.getInt("descriptions_size", MODE_PRIVATE);
            ArrayList<String> listaDescriptions = new ArrayList<String>(size);
            for (int i = 0; i < size; i++)
                listaDescriptions.add(prefDescriptions.getString("descriptions" + "_" + i, null));

            btnTimeExtend.setVisibility(View.VISIBLE);
            btnTimeExtend.setText(listaDescriptions.get(1));

            btnTimeExtend2.setVisibility(View.VISIBLE);
            btnTimeExtend2.setText(listaDescriptions.get(2));

            btnArmarAlarmes.setVisibility(View.VISIBLE);
            btnArmarAlarmes.setText(listaDescriptions.get(0));

            btnLigarDesligarLuzes.setVisibility(View.VISIBLE);
            btnLigarDesligarLuzes.setText(listaDescriptions.get(3));

            btnChamado.setVisibility(View.VISIBLE);
            btnChamado.setText(listaDescriptions.get(5));

            btnPanico.setVisibility(View.VISIBLE);
            btnPanico.setText(listaDescriptions.get(4));

            btnTimeExtend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    String title = "Solicitação de Extensão de Alarme";
                    String menssage = "Extensão de 60 minutos pela Agência ";
                    String topic = "REGIONAL";

                    new MessageTopic(topic, title, menssage);

                    auth.requestToken(nameAgencia + ".2", "true");
                }
            });

            //Desarmar Alarme
            btnTimeExtend2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    new MessageTopic(null, null, null);
                    auth.requestToken(nameAgencia + ".3", "true");
                }
            });

            btnArmarAlarmes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new MessageTopic(null, null, null);
                    auth.requestToken(nameAgencia + ".1", "true");
                }
            });

            btnLigarDesligarLuzes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new MessageTopic(null, null, null);
                    auth.requestToken(nameAgencia + ".4", "true");
                }
            });

            btnChamado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ChamadoActivity.startActivity(MainActivity.this);
                }
            });

            btnPanico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title = "Pânico Ativado";
                    String menssage = "Botão de Panico Acionado";
                    String topic = "REGIONAL";

                    new MessageTopic(topic, title, menssage);

                    auth.requestToken(nameAgencia + ".5", "true");
                }
            });
    }

    public void onBackPressed(){

        LoginActivity.startActivity(MainActivity.this);
        finish();
    }

    public void onDestroy(){
        super.onDestroy();
    }
    
    public static void startActivity(Context from) {
        Intent intent = new Intent(from, MainActivity.class);
        from.startActivity(intent);
    }
}
