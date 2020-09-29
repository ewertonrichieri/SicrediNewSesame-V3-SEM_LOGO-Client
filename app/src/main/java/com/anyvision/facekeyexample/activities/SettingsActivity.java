package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.Settings;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.facekeyexample.utils.Enum;
import com.xw.repo.BubbleSeekBar;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity {

    private BubbleSeekBar thresholdSeekBar;
    private BubbleSeekBar vidTimeSeekBar;
    private BubbleSeekBar imageCompressionSeekBar;
    private Button btnSend;
    private Button btnAtivaBtnRegisterLogin;

    private EditText server_url;
    private EditText anyvision_url;
    private Settings settings;
    private Spinner spType;
    private Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSend = (Button) findViewById(R.id.btnSend);
        spType = (Spinner) findViewById(R.id.spType);
        listenerType();
        ItemSelecionadoAnteriormente();
        settings = AppData.getSettings();

        thresholdSeekBar = findViewById(R.id.threshold_seekbar);
        vidTimeSeekBar = findViewById(R.id.vid_time_seekbar);
        imageCompressionSeekBar = findViewById(R.id.compression_seekbar);

        server_url = findViewById(R.id.server_url);
        anyvision_url = findViewById(R.id.sesame_url);

        thresholdSeekBar.setProgress(Math.round(settings.getThreshold() * 100));
        vidTimeSeekBar.setProgress(settings.getVideoTime());
        imageCompressionSeekBar.setProgress(settings.getImageCompressionRate());

        server_url.setText(GetVariables.getInstance().getServerUrl());
        anyvision_url.setText(GetVariables.getInstance().getEtAnyvisionUrl());

        btnAtivaBtnRegisterLogin = findViewById(R.id.btnAtivaBtnRegisterLogin);

        //para ativar o botão de registro na tela de login
        btnAtivaBtnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startThreadLogin();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetVariables.getInstance().setServerUrl(server_url.getText().toString());
                GetVariables.getInstance().setEtAnyvisionUrl(anyvision_url.getText().toString());
                GetVariables.getInstance().getEtLocalServerUrl().setText(server_url.getText().toString());
                GetVariables.getInstance().getTextviewAnyvision().setText(anyvision_url.getText().toString());

                GetVariables.getInstance().setSpTypeAccount(spType.getSelectedItem().toString());

                SetUrlServidorLocalSharedPrivate();
                SetUrlAnyvisionSharedPrivate();
                SetTipoAgenciaRegionalSharedPrivate();
                auth = new Authentication(GetVariables.getInstance().getServerUrl());

                Toast.makeText(SettingsActivity.this, "IP Local Servidor: " + GetVariables.getInstance().getServerUrl() +
                        "\nIP Anyvision: " + GetVariables.getInstance().getEtAnyvisionUrl() +
                        "\nTipo de Conta: " + GetVariables.getInstance().getSpTypeAccount(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        settings.setThreshold(thresholdSeekBar.getProgress() / 100f);
        settings.setVideoTime(vidTimeSeekBar.getProgress());
        settings.setImageCompressionRate(imageCompressionSeekBar.getProgress());
        settings.setBaseUrl(server_url.getText().toString());
        AppData.saveSettings();
    }

    public void listenerType() {
        addItemsType();
    }

    public void addItemsType() {
        List<String> list = new ArrayList<String>();
        list.add("AGENCIA");
        list.add("REGIONAL");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        spType.setAdapter(dataAdapter);

    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, SettingsActivity.class);
        from.startActivity(intent);
    }

    public void SetUrlServidorLocalSharedPrivate(){
        try {
            SharedPreferences shUrlServidorLocal = getSharedPreferences(Enum.SharedPrivate.URL_SERVIDOR_LOCAL.toString(), MODE_PRIVATE);
            SharedPreferences.Editor editUrlServidorLocal = shUrlServidorLocal.edit();
            editUrlServidorLocal.clear().commit();
            editUrlServidorLocal.putString(Enum.SharedPrivate.URL_SERVIDOR_LOCAL.toString(), server_url.getText().toString());
            editUrlServidorLocal.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SetUrlAnyvisionSharedPrivate(){
        try{
            SharedPreferences shUrlAnyvision = getSharedPreferences(Enum.SharedPrivate.URL_ANYVISION.toString(), MODE_PRIVATE);
            SharedPreferences.Editor editUrlAnivision = shUrlAnyvision.edit();
            editUrlAnivision.clear().commit();
            editUrlAnivision.putString(Enum.SharedPrivate.URL_ANYVISION.toString(), anyvision_url.getText().toString());
            editUrlAnivision.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void  SetTipoAgenciaRegionalSharedPrivate(){
        try {
            SharedPreferences shTipoAgenciaRegional = getSharedPreferences(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), MODE_PRIVATE);
            SharedPreferences.Editor editTipoAgReg = shTipoAgenciaRegional.edit();
            editTipoAgReg.clear().commit();
            editTipoAgReg.putString(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), spType.getSelectedItem().toString());
            editTipoAgReg.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ItemSelecionadoAnteriormente(){
        SharedPreferences shTipoAgReg = getSharedPreferences(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), MODE_PRIVATE);
        String tipoEscolhidoAnteriormente = shTipoAgReg.getString(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), null);
        if(tipoEscolhidoAnteriormente != null){
            if(tipoEscolhidoAnteriormente.equals("AGENCIA")){
                spType.setSelection(0);
            }
            else{
                spType.setSelection(1);
            }
        }
    }
}
