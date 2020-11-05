package com.anyvision.facekeyexample.activities.logged;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.login.ListaNewValueName;
import com.anyvision.facekeyexample.models.FacilitiesModel.FacilitiesModel;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.facekeyexample.utils.Enum;

import java.util.ArrayList;
import java.util.List;

public class FacilitiesControleActivity extends AppCompatActivity {

    private CalendarView calendario;
    private EditText edittxtData;
    private EditText editTxtqtdHoras;
    private EditText edtTxtQtdProfissionais;
    private ImageButton btnCalendario;
    private Button btnSelecionarData;
    private Button btnSolicitar;
    private String profissaoEscolhida;
    private CheckBox checkboxOpcao;
    private Authentication auth;
    private int numero_request;
    private String pathRequest = "App.AGENCIA.POC.AGENCIA0001.Facilities.";
    private String nomeProfissaoSeFlagSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_controle);
        calendario = findViewById(R.id.calendarioFacilities);
        calendario.setVisibility(View.GONE);
        edittxtData = findViewById(R.id.edtDataFacilities);
        editTxtqtdHoras = findViewById(R.id.editQtdHoras);
        edtTxtQtdProfissionais = findViewById(R.id.editQtdProfissionais);
        btnCalendario = findViewById(R.id.btnCalendarioFacilities);
        btnSelecionarData = findViewById(R.id.btnSelecionarData);
        btnSelecionarData.setVisibility(View.GONE);
        btnSolicitar = findViewById(R.id.btnSolicitarFacilities);
        checkboxOpcao = findViewById(R.id.checkboxOpcao);

        auth = new Authentication(GetVariables.getInstance().getServerUrl());
        profissaoEscolhida = GetVariables.getInstance().getOpcaoFacilities();

        if (profissaoEscolhida.equals(Enum.Facilities.RECEPCIONISTA.toString())){
            checkboxOpcao.setText("Bilingue");
            numero_request = 2;
            nomeProfissaoSeFlagSelecionado = "Bilingue";
        }

        if (profissaoEscolhida.equals(Enum.Facilities.VIGILANTE.toString())){
            checkboxOpcao.setText("Armado");
            numero_request = 1;
            nomeProfissaoSeFlagSelecionado = "Arma";
        }

        if (profissaoEscolhida.equals(Enum.Facilities.BOMBEIRO.toString())){
            checkboxOpcao.setText("Industrial");
            numero_request = 4;
            nomeProfissaoSeFlagSelecionado = "Industrial";
        }

        if (profissaoEscolhida.equals(Enum.Facilities.CONTROLADOR.toString())){
            checkboxOpcao.setVisibility(View.GONE);
            numero_request = 3;
        }

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendario.setVisibility(View.VISIBLE);
                btnSelecionarData.setVisibility(View.VISIBLE);
                btnSolicitar.setVisibility(View.GONE);
            }
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                edittxtData.setText(Date);
            }
        });

        btnSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendario.setVisibility(View.GONE);
                btnSelecionarData.setVisibility(View.GONE);
                btnSolicitar.setVisibility(View.VISIBLE);
            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> li = new ArrayList<String>();

                li.add(pathRequest + numero_request + ";" + edtTxtQtdProfissionais.getText().toString());
                li.add(pathRequest + numero_request + "_data;"+edittxtData.getText().toString());
                li.add(pathRequest + numero_request + "_horas;"+editTxtqtdHoras.getText().toString());
                if(profissaoEscolhida != Enum.Facilities.CONTROLADOR.toString()){
                    int simNao = checkboxOpcao.isChecked() == true ? 1 : 0;
                    li.add(pathRequest + numero_request + "_" + nomeProfissaoSeFlagSelecionado+";" + simNao);
                }

                auth.requestTokenFacilities(li);
            }
        });

    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, FacilitiesControleActivity.class);
        from.startActivity(intent);
    }

    public void onBackPressed() {
        FacilitiesActivity.startActivity(FacilitiesControleActivity.this);
    }
}