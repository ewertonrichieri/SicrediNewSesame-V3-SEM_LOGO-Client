package com.anyvision.facekeyexample.activities.logged;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.MessageTopic;
import com.anyvision.facekeyexample.prysm.Authentication;

import java.util.ArrayList;

public class ChamadoActivity extends AppCompatActivity {

    private Button btnCFTV;
    private Button btbAlarmeChamado;
    private Button btnSistemaIncendio;
    private Button btnIluminacao;
    private Button btnArCondicionado;
    private String typeAccount;
    private Authentication auth;
    private String chamadoDescription = "App.AGENCIA.POC.AGENCIA0001.6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado);

        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        typeAccount = GetVariables.getInstance().getSpTypeAccount();

        SharedPreferences sharedTypeAccount = getSharedPreferences("typeAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedTypeAccount.edit();
        editor.clear().commit();
        editor.putString("typeAccount", typeAccount);
        editor.apply();

        btnCFTV = findViewById(R.id.btnCFTV);
        btbAlarmeChamado = findViewById(R.id.btbAlarmeChamado);
        btnSistemaIncendio = findViewById(R.id.btnSistemaIncendio);
        btnIluminacao = findViewById(R.id.btnHVAC);
        btnArCondicionado = findViewById(R.id.btnArCondicionado);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int size = sharedPreferences.getInt("chamadoDescriptions_size", MODE_PRIVATE);
        final ArrayList<String> listaChamadoDescriptions = new ArrayList<String>(size);

        for (int i = 0; i < size; i++) {
            listaChamadoDescriptions.add(sharedPreferences.getString("chamado" + "_" + i, null));
        }

        btnCFTV.setText(listaChamadoDescriptions.get(1));
        btbAlarmeChamado.setText(listaChamadoDescriptions.get(2));
        btnSistemaIncendio.setText(listaChamadoDescriptions.get(3));
        btnIluminacao.setText(listaChamadoDescriptions.get(4));
        btnArCondicionado.setText(listaChamadoDescriptions.get(5));

        btnCFTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    new MessageTopic(null, null, null);
                    auth.requestToken(chamadoDescription, "1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btbAlarmeChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    new MessageTopic(null, null, null);
                    auth.requestToken(chamadoDescription, "2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSistemaIncendio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    new MessageTopic(null, null, null);
                    auth.requestToken(chamadoDescription, "3");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnIluminacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    new MessageTopic(null, null, null);
                    auth.requestToken(chamadoDescription, "4");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnArCondicionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());

                    new MessageTopic(null, null, null);
                    auth.requestToken(chamadoDescription, "5");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, ChamadoActivity.class);
        from.startActivity(intent);
    }
}
