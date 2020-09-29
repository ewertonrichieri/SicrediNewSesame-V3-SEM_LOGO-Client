package com.anyvision.facekeyexample.activities.logged;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.MessageTopic;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.facekeyexample.utils.Enum;
import com.anyvision.facekeyexample.utils.NewAdapter;

import java.util.ArrayList;

public class SolicitationExtensionActivity extends AppCompatActivity {

    private Authentication auth;
    private static String name;
    private static String newValue;
    private ArrayList<String> listaDescriptions;
    private static ArrayList<String> liHistApproved;
    private static ArrayList<String> liHistReproved;
    private RecyclerView.Adapter adapter;
    private static boolean enableProgressBarVisible = true;
    private Paint p = new Paint();
    private int LEFT = 4;
    private int RIGHT = 8;
    private int aprovado = 1;
    private int reprovado = 2;
    private Button btnAprovado;
    private Button btnReprovado;
    private TextView serverLocalUrl;
    private TextView txtSolicitation;
    private static boolean key = true;
    private static Thread solicitationThread = null;
    private static ProgressBar progressBar;
    private static boolean AllowGetlistSolicitation;
    private static boolean refresh;
    private static Activity finishSolicitationExtension;
    private static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitation_extension);

        btnAprovado = findViewById(R.id.btnAprovado);
        btnReprovado = findViewById(R.id.btnReprovado);
        serverLocalUrl = findViewById(R.id.serverLocalUrl);

        progressBar = findViewById(R.id.progressBarSolicit);
        progressBar.setVisibility(View.GONE);
        txtSolicitation = findViewById(R.id.txtSolicitation);
        txtSolicitation.setVisibility(View.INVISIBLE);

        finishSolicitationExtension = this;
        AllowGetlistSolicitation = true;

        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        if (GetVariables.getInstance().getServerUrl() == null)
            GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());

        btnAprovado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (liHistApproved.size() == 0)
                    auth.requestToken("aprovaReprovaExtesao", Enum.request.state.toString());
                SolicitationHistoryApproved.startActivity(SolicitationExtensionActivity.this);
            }
        });

        btnReprovado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (liHistReproved.size() == 0)
                    auth.requestToken("aprovaReprovaExtesao", Enum.request.state.toString());
                SolicitationHistoryReproved.startActivity(SolicitationExtensionActivity.this);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerViewMain);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences prefDescriptions = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int size = prefDescriptions.getInt("solicitacao_size", MODE_PRIVATE);
        listaDescriptions = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            listaDescriptions.add(prefDescriptions.getString("solicitacao" + "_" + i, null).replace("App.", ""));
        }

        if ((listaDescriptions == null) || listaDescriptions.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        }
        adapter = new NewAdapter(listaDescriptions, SolicitationExtensionActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //list historyAproved
        SharedPreferences prefHistApproved = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int sizeLiHistApproved = prefHistApproved.getInt("solicitacaoAprovada_size", MODE_PRIVATE);
        final ArrayList<String> liApproved = new ArrayList<String>(sizeLiHistApproved);
        for (int i = 0; i < sizeLiHistApproved; i++) {
            liApproved.add(prefHistApproved.getString("solicitacaoAprovada" + "_" + i, null));
            Log.d("arrayHist", liApproved.get(i));
        }

        liHistApproved = liApproved;

        //list historyReproved
        SharedPreferences prefHistReproved = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int sizeLiHistReproved = prefHistReproved.getInt("solicitacaoReprovada_size", MODE_PRIVATE);
        final ArrayList<String> liReproveed = new ArrayList<String>(sizeLiHistReproved);
        for (int i = 0; i < sizeLiHistReproved; i++) {
            liReproveed.add(prefHistReproved.getString("solicitacaoReprovada" + "_" + i, null));
            Log.d("arrayHist", liReproveed.get(i));
        }

        liHistReproved = liReproveed;
        solicitationThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (AllowGetlistSolicitation) {
                                    AllowGetlistSolicitation = false;

                                    final SharedPreferences prefDescriptions = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    int size = prefDescriptions.getInt("solicitacao_size", MODE_PRIVATE);
                                    ArrayList<String> listaDescription = new ArrayList<String>(size);
                                    for (int i = 0; i < size; i++) {
                                        listaDescription.add(prefDescriptions.getString("solicitacao" + "_" + i, null));
                                    }

                                    if (listaDescription.size() == 0)
                                        listaDescription = listaDescriptions;

                                    if (listaDescription.size() > listaDescriptions.size()) {
                                        for (int i = 0; i < listaDescription.size(); i++) {
                                            if (!listaDescriptions.contains(listaDescription.get(i))) {
                                                listaDescriptions.add(0, listaDescription.get(i).replace("App.", ""));
                                                adapter.notifyDataSetChanged();
                                                if (listaDescriptions.size() == 0) {
                                                    setAllowGetlistSolicitation();
                                                } else {
                                                }
                                                adapter.notifyItemInserted(0);
                                                if (refresh) {
                                                    refresh = false;
                                                }
                                            }
                                            if (adapter.getItemCount() != listaDescriptions.size()) {
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    if (refresh) {
                                        refresh = false;
                                    }
                                    if (!enableProgressBarVisible)
                                    progressBar.setVisibility(View.GONE);
                                    enableProgressBarVisible = true;
                                    if (listaDescriptions.size() == 0)
                                        txtSolicitation.setVisibility(View.VISIBLE);
                                    else {
                                        txtSolicitation.setVisibility(View.GONE);
                                    }
                                }
                                if (listaDescriptions.size() == 0) {
                                    txtSolicitation.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    txtSolicitation.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        solicitationThread.start();
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                try {
                    if (auth.getStatusServer()) {
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                            Bitmap icon;
                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;

                            if (dX > 0) {
                                p.setColor(Color.parseColor("#388E3C"));
                                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop() + 2, dX, (float) itemView.getBottom() - 2);
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ok_new);
                                RectF icon_dest = new RectF((float) itemView.getLeft() + width + 40, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width + 40, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);

                            } else {
                                p.setColor(Color.parseColor("#D32F2F"));
                                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop() + 2, (float) itemView.getRight(), (float) itemView.getBottom() - 2);
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_g);
                                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width - 40, (float) itemView.getTop() + width + 5, (float) itemView.getRight() - width - 40, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            }
                        }
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    } else {
                        Toast.makeText(SolicitationExtensionActivity.this, "Por favor, verifique o status do servidor local!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                try {
                    if (auth.getStatusServer()) {
                        //direction 4 is Left and direction 8 is Right
                        int pos = viewHolder.getAdapterPosition();
                        String nmAgenciaSelecionada = String.valueOf(listaDescriptions.get(pos));

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.clear().commit();

                        if (direction == LEFT) {
                            String title = "Solicitação Reprovada";
                            String message = "A extensão não foi aceita";
                            String topic = "AGENCIA";

                            new MessageTopic(topic, title, message);
                            Log.d("mensagem_Firebase", String.valueOf(direction) + "armazenou no message topic");
                        }

                        if (direction == RIGHT) {
                            String title = "Solicitação Aprovada";
                            String message = "A extensão foi aceita";
                            String topic = "AGENCIA";

                            new MessageTopic(topic, title, message);
                            Log.d("mensagem_Firebase", String.valueOf(direction) + "armazenou no message topic");
                        }

                        //O valor vai vir Solicita.000*, esta função divide Solicita / 000 para gravar no array abaixo e fazer a requisição no auth.request() com a solicitação correta.
                        String teste[] = nmAgenciaSelecionada.split("\\.");
                        //pega somente o nome da agencia
                        String refNameAgencia = teste[1];

                        //if left then reprovado else aprovado
                        newValue = String.valueOf(direction == LEFT ? reprovado : aprovado);

                        auth.requestToken("App.REGIONAL.POC.AGENCIA" + refNameAgencia + ".Reprogramacao", newValue);

                        listaDescriptions.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    public static void setAllowGetlistSolicitation() {
        AllowGetlistSolicitation = true;
        enableProgressBarVisible = false;
        Log.d("FirebaseNotify", "key = true");
    }

    public static void refreshActivity() {
        refresh = true;
    }

    public void onBackPressed() {
        LoginActivity.startActivity(SolicitationExtensionActivity.this);
        GetVariables.getInstance().setSpTypeAccount("REGIONAL");
        eraserSharedPreferences();
        finish();
    }

    public void eraserSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }

    public void onResume() {
        super.onResume();
        auth.requestToken("aprovaReprovaExtesao", "geral");
        active = true;
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
        solicitationThread.interrupt();
        Thread.interrupted();
    }

    public void onDestroy() {
        active = false;
        super.onDestroy();
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, SolicitationExtensionActivity.class);
        from.startActivity(intent);
    }

    public static Activity getInstance() {
        return finishSolicitationExtension;
    }
    public static boolean onActive() {
        return active;
    }
}
