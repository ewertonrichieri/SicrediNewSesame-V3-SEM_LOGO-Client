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
import android.widget.ImageButton;
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
import com.anyvision.facekeyexample.utils.SolicitHistReprovedAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SolicitationHistoryReproved extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private ImageButton btnVoltar;
    private ImageButton btnHistBackSolicit_Reproved;
    private Authentication auth;
    private ArrayList<String> listSolicitHistory;
    private ProgressBar progressBar;
    private static boolean AllowGetlistSolicitHist;
    private static Thread solicitationHistThread = null;
    private static boolean enableProgressBarVisible = true;
    private static Activity finishSolicitationHistoryReproved;
    private static boolean active = false;
    private TextView txtMenuAprovReprov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitation_history);
        btnVoltar = findViewById(R.id.btnHistBackSolicit_Reproved);
        progressBar = findViewById(R.id.progressBarHistory);
        progressBar.setVisibility(View.GONE);
        txtMenuAprovReprov = findViewById(R.id.txtMenuAprovReprov);
        txtMenuAprovReprov.setText(getString(R.string.REPROVADO));
        btnHistBackSolicit_Reproved = findViewById(R.id.btnHistBackSolicit);
        btnHistBackSolicit_Reproved.setVisibility(View.GONE);
        AllowGetlistSolicitHist = true;
        finishSolicitationHistoryReproved = this;
        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        final RecyclerView recyclerView = findViewById(R.id.recyclerViewHistorySolicitation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences prefListHistorics = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int size = prefListHistorics.getInt("solicitacaoReprovada_size", MODE_PRIVATE);
        listSolicitHistory = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            listSolicitHistory.add(prefListHistorics.getString("solicitacaoReprovada" + "_" + i, null));
        }

        if ((listSolicitHistory == null) || listSolicitHistory.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        }

        adapter = new SolicitHistReprovedAdapter(listSolicitHistory, SolicitationHistoryReproved.this);
        recyclerView.setAdapter(adapter);

        solicitationHistThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                       Log.d("solicitationThread", "History_looping_Thread");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (AllowGetlistSolicitHist) {
                                    AllowGetlistSolicitHist = false;

                                    final SharedPreferences prefDescriptions = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    int size = prefDescriptions.getInt("solicitacaoReprovada_size", MODE_PRIVATE);
                                    final ArrayList<String> listaDescription = new ArrayList<String>(size);
                                    for (int i = 0; i < size; i++) {
                                        listaDescription.add(prefDescriptions.getString("solicitacaoReprovada" + "_" + i, null));
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
                SolicitationExtensionActivity.startActivity(SolicitationHistoryReproved.this);
                solicitationHistThread.interrupt();
            }
        });
    }

    //Habilita a Thread
    public static void setAllowGetlistSolicitHist() {
        AllowGetlistSolicitHist = true;
        enableProgressBarVisible = false;
    }

    public void eraserSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }

    public void onResume(){
        super.onResume();
        auth.requestToken("aprovaReprovaExtesao", "solicitationExtension");
        active = true;
    }

    public void onPause(){
        super.onPause();
    }

    public void onStop(){
        super.onStop();
        solicitationHistThread.interrupt();
    }

    //teste
    public void onDestroy(){
        active = false;
        super.onDestroy();
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, SolicitationHistoryReproved.class);
        from.startActivity(intent);
    }

    public static Activity getInstance(){
        return finishSolicitationHistoryReproved;
    }

    public static boolean onActive(){
        return active;
    }

    public static void setOnActive(boolean setActive){
        active = setActive;
    }
}
