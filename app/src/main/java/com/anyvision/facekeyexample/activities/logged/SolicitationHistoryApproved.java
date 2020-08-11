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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.facekeyexample.utils.SolicitHistAprovAdapter;

import java.util.ArrayList;

public class SolicitationHistoryApproved extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private Button btnVoltar;
    private Button btnHome;
    private Authentication auth;
    private ArrayList<String> listSolicitHistory;
    private ProgressBar progressBar;
    private static boolean AllowGetlistSolicitHist;
    private static Thread solicitationHistThread = null;
    private static Activity finishSolicitationHistoryApproved;
    private static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitation_history);
        btnVoltar = findViewById(R.id.btnHistBackSolicit);
        btnHome = findViewById(R.id.btnHistBackHomeLogin);
        progressBar = findViewById(R.id.progressBarHistory);
        progressBar.setVisibility(View.GONE);
        AllowGetlistSolicitHist = true;
        finishSolicitationHistoryApproved = this;

        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        final RecyclerView recyclerView = findViewById(R.id.recyclerViewHistorySolicitation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences prefListHistorics = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int size = prefListHistorics.getInt("solicitacaoAprovada_size", MODE_PRIVATE);
        listSolicitHistory = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            listSolicitHistory.add(prefListHistorics.getString("solicitacaoAprovada" + "_" + i, null));
        }

        if ((listSolicitHistory == null) || listSolicitHistory.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        }

//        auth.requestToken("aprovaReprovaExtesao", "solicitationExtension");

        adapter = new SolicitHistAprovAdapter(listSolicitHistory, SolicitationHistoryApproved.this);
        recyclerView.setAdapter(adapter);

        solicitationHistThread = new Thread() {
            //        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        Log.d("solicitationThread", "HistoryAPROVADO_looping_Thread");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (AllowGetlistSolicitHist) {
                                    AllowGetlistSolicitHist = false;

                                    final SharedPreferences prefDescriptions = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    int size = prefDescriptions.getInt("solicitacaoAprovada_size", MODE_PRIVATE);
                                    final ArrayList<String> listaDescription = new ArrayList<String>(size);
                                    for (int i = 0; i < size; i++) {
                                        listaDescription.add(prefDescriptions.getString("solicitacaoAprovada" + "_" + i, null));
                                    }

                                    if (listaDescription.size() > listSolicitHistory.size()) {

                                        for (int i = 0; i < listaDescription.size(); i++) {

                                            if (!listSolicitHistory.contains(listaDescription.get(i))) {
                                                listSolicitHistory.add(listaDescription.get(i));
                                                adapter.notifyItemInserted(listSolicitHistory.size());
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    //quando inicia essa tela e listaDescription esta vazio,ele entra aqui e remove tudo esta errado corrigir, o listSolicitHistory tinha 6
                                    if (listaDescription.size() < listSolicitHistory.size() && listaDescription.size() > 0) {
                                        try {

                                            for (int i = 0; i < listSolicitHistory.size(); i++) {

                                                if (!listaDescription.contains(listSolicitHistory.get(i))) {
                                                    listSolicitHistory.remove(listSolicitHistory.get(i));
                                                    //ele esta removendo zerado aqui
                                                    adapter.notifyItemRemoved(i);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        solicitationHistThread.start();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SolicitationExtensionActivity.refreshActivity();
                SolicitationExtensionActivity.startActivity(SolicitationHistoryApproved.this);
                solicitationHistThread.interrupt();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SolicitationExtensionActivity.onActive()){
                    SolicitationExtensionActivity.getInstance().finish();
                }

                if (SolicitationHistoryReproved.onActive())
                    SolicitationHistoryReproved.getInstance().finish();

                LoginActivity.startActivity(SolicitationHistoryApproved.this);
                GetVariables.getInstance().setSpTypeAccount("REGIONAL");
                eraserSharedPreferences();

                finish();
            }
        });
    }

    //Habilita a Thread
    public static void setAllowGetlistSolicitHist() {
        AllowGetlistSolicitHist = true;
        Log.d("threadSolicitation", "key = true");
    }

    public void eraserSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }

    public void onResume() {
        super.onResume();
        auth.requestToken("aprovaReprovaExtesao", "solicitationExtension");
        active = true;
        Log.d("solicitationThread", "History_onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d("solicitationThread", "History_onPause");
    }

    public void onStop() {
        super.onStop();
        solicitationHistThread.interrupt();
        Log.d("solicitationThread", "History_onStop");
    }

    //teste
    public void onDestroy() {
        active = false;
        super.onDestroy();
        Log.d("destruindo", "SolicitationHistoryApproved");

    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, SolicitationHistoryApproved.class);
        from.startActivity(intent);
    }

    public static boolean onActive() {
        return active;
    }

    public static void setOnActive(boolean setActive){
        active = setActive;
    }

    public static Activity getInstance() {
        return finishSolicitationHistoryApproved;
    }
}
