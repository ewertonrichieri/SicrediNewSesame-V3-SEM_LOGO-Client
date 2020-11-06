package com.anyvision.facekeyexample.activities.logged;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView txtInfoTelaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_controle);
        calendario = findViewById(R.id.calendarioFacilities);
        calendario.setVisibility(View.GONE);
        calendario.setMinDate(System.currentTimeMillis() - 1000);
        edittxtData = findViewById(R.id.edtDataFacilities);
        editTxtqtdHoras = findViewById(R.id.editQtdHoras);
        edtTxtQtdProfissionais = findViewById(R.id.editQtdProfissionais);
        btnCalendario = findViewById(R.id.btnCalendarioFacilities);
        btnSelecionarData = findViewById(R.id.btnSelecionarData);
        btnSelecionarData.setVisibility(View.GONE);
        btnSolicitar = findViewById(R.id.btnSolicitarFacilities);
        checkboxOpcao = findViewById(R.id.checkboxOpcao);
        txtInfoTelaAtual = findViewById(R.id.txtInfoTelaProfissao);

        auth = new Authentication(GetVariables.getInstance().getServerUrl());
        profissaoEscolhida = GetVariables.getInstance().getOpcaoFacilities();

        if (profissaoEscolhida.equals(Enum.Facilities.RECEPCIONISTA.toString())) {
            checkboxOpcao.setText("Bilingue");
            numero_request = 2;
            nomeProfissaoSeFlagSelecionado = "Bilingue";
            txtInfoTelaAtual.setText("Solicitação de Recepcionista");
        }

        if (profissaoEscolhida.equals(Enum.Facilities.VIGILANTE.toString())) {
            checkboxOpcao.setText("Armado");
            numero_request = 1;
            nomeProfissaoSeFlagSelecionado = "Arma";
            txtInfoTelaAtual.setText("Solicitação de Vigilante");
        }

        if (profissaoEscolhida.equals(Enum.Facilities.BOMBEIRO.toString())) {
            checkboxOpcao.setText("Industrial");
            numero_request = 4;
            nomeProfissaoSeFlagSelecionado = "Industrial";
            txtInfoTelaAtual.setText("Solicitação de Bombeiro");
        }

        if (profissaoEscolhida.equals(Enum.Facilities.CONTROLADOR.toString())) {
            checkboxOpcao.setVisibility(View.GONE);
            numero_request = 3;
            txtInfoTelaAtual.setText("Solicitação de Controlador de Acesso");
        }

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendario.setVisibility(View.VISIBLE);
                btnSelecionarData.setVisibility(View.VISIBLE);
                btnSolicitar.setVisibility(View.GONE);
                checkboxOpcao.setVisibility(View.GONE);
                edtTxtQtdProfissionais.setVisibility(View.GONE);

                InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null)
                im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        edittxtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    calendario.setVisibility(View.VISIBLE);
                    btnSelecionarData.setVisibility(View.VISIBLE);
                    btnSolicitar.setVisibility(View.GONE);
                    checkboxOpcao.setVisibility(View.GONE);
                    edtTxtQtdProfissionais.setVisibility(View.GONE);
                    //teste
                    InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    edittxtData.setInputType(InputType.TYPE_NULL);

                }
            }
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String[] meses = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
                String mesSelecionado = meses[month];
                String Date = dayOfMonth + "/" + (mesSelecionado) + "/" + year;
                edittxtData.setText(Date);
                //String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
            }
        });

        btnSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendario.setVisibility(View.GONE);
                btnSelecionarData.setVisibility(View.GONE);
                btnSolicitar.setVisibility(View.VISIBLE);
                checkboxOpcao.setVisibility(View.VISIBLE);
                edtTxtQtdProfissionais.setVisibility(View.VISIBLE);
            }
        });

//        edittxtData.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                calendario.setVisibility(View.VISIBLE);
//                btnSelecionarData.setVisibility(View.VISIBLE);
//                btnSolicitar.setVisibility(View.GONE);
//                checkboxOpcao.setVisibility(View.GONE);
//                edtTxtQtdProfissionais.setVisibility(View.GONE);
//            }
//        });
        edittxtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittxtData.setInputType(InputType.TYPE_CLASS_TEXT);
                edittxtData.requestFocus();
                InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(edittxtData, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    auth.verifyServerStatus();
                    if (auth.getStatusServer()) {

                        if ((edittxtData.getText().toString().isEmpty()) || (editTxtqtdHoras.getText().toString().isEmpty())
                                || (edtTxtQtdProfissionais.getText().toString().isEmpty())) {
                            Toast.makeText(FacilitiesControleActivity.this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String regex = "[0-9]+";
                        Boolean validaValor = edtTxtQtdProfissionais.getText().toString().matches(regex);
                        if (!validaValor) {
                            edtTxtQtdProfissionais.setError("Digite apenas um número de 1 a 9");
                            return;
                        }

                        int verificaValorUmNove = Integer.parseInt(edtTxtQtdProfissionais.getText().toString());
                        if ((verificaValorUmNove < 1) || verificaValorUmNove > 9) {
                            edtTxtQtdProfissionais.setError("Digite apenas um número de 1 a 9");
                            return;
                        }

                        ArrayList<String> li = new ArrayList<String>();

                        li.add(pathRequest + numero_request + "_data;" + edittxtData.getText().toString());
                        li.add(pathRequest + numero_request + "_horas;" + editTxtqtdHoras.getText().toString());
                        if (profissaoEscolhida != Enum.Facilities.CONTROLADOR.toString()) {
                            int simNao = checkboxOpcao.isChecked() == true ? 1 : 0;
                            li.add(pathRequest + numero_request + "_" + nomeProfissaoSeFlagSelecionado + ";" + simNao);
                        }

                        li.add(pathRequest + numero_request + ";" + edtTxtQtdProfissionais.getText().toString());

                        auth.requestTokenFacilities(li);
                    } else {
                        Toast.makeText(FacilitiesControleActivity.this, getString(R.string.verifique_status_servidor), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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