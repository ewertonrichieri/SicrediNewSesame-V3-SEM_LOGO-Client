package com.anyvision.facekeyexample.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.models.GetVariables;
import com.google.firebase.FirebaseApp;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etNome;
    private Spinner spCargo;
    private Spinner spAgencia;
    private Button btnNext;
    private Button btnPrevious;
    private static Activity finishRegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(getApplicationContext());

        finishRegisterActivity = this;
        etUsername = (EditText) findViewById(R.id.etUsername);
        etNome = (EditText) findViewById(R.id.etName);
        spCargo = (Spinner) findViewById(R.id.spCargo);
        spAgencia = (Spinner) findViewById(R.id.spAgencia);
        btnNext = (Button) findViewById(R.id.btNext);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        listenerCargo();
        listenerAgencia();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = etUsername.getText().toString();
                    String nome = etNome.getText().toString();
                    GetVariables.getInstance().setEtRegisterUsername(username);

                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(nome)) {
                        Toast.makeText(RegisterActivity.this, "Por favor, preencha os campos vazios!", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        GetVariables.getInstance().setEtRegisterUsername(username);
                        GetVariables.getInstance().setNameRegister(nome);
                        GetVariables.getInstance().setCargoRegister(spCargo.getSelectedItem().toString());
                        GetVariables.getInstance().setLocalAgenciaRegister(spAgencia.getSelectedItem().toString());

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().commit();

                        InstructionsActivity.startActivity(RegisterActivity.this);
                    }

                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startActivity(RegisterActivity.this);
                finish();
            }
        });
    }

    public void listenerCargo() {
        addItemsCargos();
    }

    public void addItemsCargos() {
        List<String> list = new ArrayList<String>();
        list.add("Gerente Agência");
        list.add("Gerente Regional");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);

        spCargo.setAdapter(dataAdapter);
    }

    public void listenerAgencia() {
        addItemsAgencias();
    }

    public void addItemsAgencias() {
        List<String> list = new ArrayList<String>();
        list.add("Agência POC");
        list.add("Agência 2");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        spAgencia.setAdapter(dataAdapter);
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, RegisterActivity.class);
        from.startActivity(intent);
    }

    public void onStop() {
        super.onStop();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        LoginActivity.startActivity(RegisterActivity.this);
        finish();
    }

    public void onRestart() {
        super.onRestart();
    }

    public static Activity getInstance() {
        return finishRegisterActivity;
    }
}
