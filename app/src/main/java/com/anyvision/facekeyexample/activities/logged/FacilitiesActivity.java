package com.anyvision.facekeyexample.activities.logged;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.utils.Enum;

import java.util.ArrayList;

public class FacilitiesActivity extends AppCompatActivity {

    private Button btnVigilante;
    private Button btnRecepcionista;
    private Button btnControladorAcesso;
    private Button btnBombeiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_facilities);
            btnVigilante = findViewById(R.id.btnVigilante);
            btnRecepcionista = findViewById(R.id.btnRecepcionista);
            btnControladorAcesso = findViewById(R.id.btnControladorAcesso);
            btnBombeiro = findViewById(R.id.btnBombeiro);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int size = sharedPreferences.getInt("facilitiesDescricaoBtn_size", MODE_PRIVATE);
            final ArrayList<String> listaFacilitiesDescricaoBtn = new ArrayList<String>(size);

            for (int i = 0; i < size; i++) {
                listaFacilitiesDescricaoBtn.add(sharedPreferences.getString("facilitiesDescricaoBtn" + "_" + i, null));
            }

            if(listaFacilitiesDescricaoBtn.size() > 0){
            btnVigilante.setText(listaFacilitiesDescricaoBtn.get(0));
            btnRecepcionista.setText(listaFacilitiesDescricaoBtn.get(1));
            btnControladorAcesso.setText(listaFacilitiesDescricaoBtn.get(2));
            btnBombeiro.setText(listaFacilitiesDescricaoBtn.get(3));
            }

            btnVigilante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        GetVariables.getInstance().setTxtOpcaoFacilities(Enum.Facilities.VIGILANTE.toString());
                        FacilitiesControleActivity.startActivity(FacilitiesActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnRecepcionista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        GetVariables.getInstance().setTxtOpcaoFacilities(Enum.Facilities.RECEPCIONISTA.toString());
                        FacilitiesControleActivity.startActivity(FacilitiesActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnControladorAcesso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        GetVariables.getInstance().setTxtOpcaoFacilities(Enum.Facilities.CONTROLADOR.toString());
                        FacilitiesControleActivity.startActivity(FacilitiesActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnBombeiro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        GetVariables.getInstance().setTxtOpcaoFacilities(Enum.Facilities.BOMBEIRO.toString());
                        FacilitiesControleActivity.startActivity(FacilitiesActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, FacilitiesActivity.class);
        from.startActivity(intent);
    }

    public void onBackPressed() {
        GestaoActivity.startActivity(FacilitiesActivity.this);
    }
}