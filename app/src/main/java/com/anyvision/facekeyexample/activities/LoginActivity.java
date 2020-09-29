package com.anyvision.facekeyexample.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.logged.SolicitationExtensionActivity;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.facekeyexample.utils.Enum;
import com.anyvision.sesame.Sesame;
import com.google.firebase.messaging.FirebaseMessaging;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity {

    private Settings settings = new Settings();
    private static final int TIMEOUT = 120000;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private Button signUpBtn;
    private Button logInBtn;
    private Button btnPanico;
    private View progressBar;
    private InfoMobile infoMobile;
    private TextView serverLocalUrl;
    private TextView anyvisionUrl;
    private View SettingsComponent;
    private EditText etUsername;
    private Authentication auth;
    private static Thread LoginActivityThread;
    private static boolean enableBtnRegister;
    private int countClick = 0;
    private long clickDelayTime = 1000;
    private CountDownTimer mCountDownTimer = new CountDownTimer(clickDelayTime, clickDelayTime) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            countClick = 0;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        infoMobile = new InfoMobile();
        SettingsComponent = findViewById(R.id.settings);
        logInBtn = findViewById(R.id.login);
        signUpBtn = findViewById(R.id.signup);
        btnPanico = findViewById(R.id.loginPanico);
        serverLocalUrl = findViewById(R.id.serverLocalUrl);
        anyvisionUrl = findViewById(R.id.anyvisionUrl);
        etUsername = findViewById(R.id.username);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);
        final EditText input = (EditText) view.findViewById(R.id.txtsenha);
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String senha = input.getText().toString();
                if (senha.equals(getString(R.string.password))) {
                    SettingsActivity.startActivity(LoginActivity.this);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("SAIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        SharedPreferences sharedEnableBtn = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedEnableBtn.getBoolean(getString(R.string.enableBtnRegister), false)) {
            enableBtnRegister = true;
        }

        if (enableBtnRegister) {
            signUpBtn.setVisibility(View.VISIBLE);
        } else {
            signUpBtn.setVisibility(View.GONE);
        }

        LoginActivityThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (enableBtnRegister) {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (enableBtnRegister) {
                                    signUpBtn.setVisibility(View.VISIBLE);
                                    Log.d("LoginThread", "LoginThread looping");
                                } else {
                                    signUpBtn.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        GetVariables.getInstance().setEtLocalServerUrl(serverLocalUrl);
        GetVariables.getInstance().setTextviewAnyvision(anyvisionUrl);

        GetUrlTipoAgenciaRegional();
        GetUrlServidorLocalAlterada();
        GetUrlAnyvisionAlterada();

        if (GetVariables.getInstance().getSpTypeAccount() == getString(R.string.REGIONAL)) {
            try {
                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.REGIONAL));
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());
        GetVariables.getInstance().setEtAnyvisionUrl(anyvisionUrl.getText().toString());
        progressBar = findViewById(R.id.progress_bar);
        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        btnPanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    auth.requestToken(getString(R.string.AGENCIA0001_5), String.valueOf(true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Sesame.initialize(anyvisionUrl.getText().toString(), 60000);
                RegisterActivity.startActivity(LoginActivity.this);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());
                auth.verifyServerStatus();
                try {
                    if (auth.getStatusServer()) {
                        if (etUsername.getText().toString().matches("")) {
                            Toast.makeText(LoginActivity.this, getString(R.string.digite_nome_usuario), Toast.LENGTH_LONG).show();
                        } else {
                            GetVariables.getInstance().setEtUsername(etUsername.getText().toString());
                            progressBar.setVisibility(View.VISIBLE);
                            Sesame.initialize(anyvisionUrl.getText().toString(), 60000);
                            String typeAccount = GetVariables.getInstance().getSpTypeAccount();

                            if (typeAccount.equals(Enum.AgReg.REGIONAL.toString())) {
                                auth.requestToken(getString(R.string.aprovaReprovaExtesao), getString(R.string.geral));
                            }

                            if (typeAccount.equals(Enum.AgReg.AGENCIA.toString())) {
                                auth.requestToken(Enum.request.aprovaReprovaExtesao.toString(), Enum.request.descriptions.toString());
                            }

                            LoginCameraActivity.startActivity(LoginActivity.this);
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, getString(R.string.verifique_status_servidor), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                try {
                    countClick++;
                    mCountDownTimer.cancel();
                    mCountDownTimer.start();

                    if (countClick >= 7) {
                        input.getText().clear();
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        getPermissions();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (!Settings.System.canWrite(this)) {
                showBrightnessPermissionDialog();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        removeProgress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
                }
            }
        }
    }

    private void getPermissions() {
        boolean hasPermissions = true;
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermissions = false;
                break;
            }
        }

        if (!hasPermissions) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
        }
    }

    private void removeProgress() {
        progressBar.setVisibility(View.GONE);
        signUpBtn.setEnabled(true);
    }

    private void showBrightnessPermissionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.writing_permission_alert_title));
        builder.setMessage(getString(R.string.writing_permission_alert_message))
                .setPositiveButton(getString(R.string.cont), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public static void startThreadLogin() {
        enableBtnRegister = true;
        LoginActivityThread.start();
    }

    public static void stopThreadLogin() {
        enableBtnRegister = false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean result = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            result = true;
        }

        return result;
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, LoginActivity.class);
        from.startActivity(intent);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void GetUrlAnyvisionAlterada() {
        try {
            SharedPreferences shUrlAnyvision = getSharedPreferences(Enum.SharedPrivate.URL_ANYVISION.toString(), MODE_PRIVATE);
            String urlAnyvision = shUrlAnyvision.getString(Enum.SharedPrivate.URL_ANYVISION.toString(), null);

            if (urlAnyvision != null) {
                GetVariables.getInstance().setEtAnyvisionUrl(urlAnyvision);
                anyvisionUrl.setText(urlAnyvision);
            }
            else {
                GetVariables.getInstance().setEtAnyvisionUrl(anyvisionUrl.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetUrlServidorLocalAlterada() {
        try {
            SharedPreferences shUrlServidorLocal = getSharedPreferences(Enum.SharedPrivate.URL_SERVIDOR_LOCAL.toString(), MODE_PRIVATE);
            String urlServidorLocal = shUrlServidorLocal.getString(Enum.SharedPrivate.URL_SERVIDOR_LOCAL.toString(), null);

            if (urlServidorLocal != null) {
                GetVariables.getInstance().setServerUrl(urlServidorLocal);
                serverLocalUrl.setText(urlServidorLocal);
            } else {
                GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void GetUrlTipoAgenciaRegional() {
        try {
            SharedPreferences shTipoAgenciaRegional = getSharedPreferences(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), MODE_PRIVATE);
            String tipoAgenciaReginoal = shTipoAgenciaRegional.getString(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), null);

            if (tipoAgenciaReginoal != null) {
                GetVariables.getInstance().setSpTypeAccount(tipoAgenciaReginoal);
            }

            if (GetVariables.getInstance().getSpTypeAccount() == null) {
                Firebase.getTypeFirebase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
