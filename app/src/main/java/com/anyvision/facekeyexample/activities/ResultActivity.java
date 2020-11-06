package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.firebase.StringsFirebase;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.utils.Enum;
import com.anyvision.sesame.Sesame;
import com.anyvision.sesame.listeners.FragmentCommunicatior;
import com.anyvision.sesame.listeners.IRegisterListener;
import com.anyvision.sesame.listeners.eFragmentChooser;
import com.anyvision.sesame.utils.AnvSurfaceType;
import java.io.File;
import java.text.SimpleDateFormat;

public class ResultActivity extends BaseActivity implements FragmentCommunicatior {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    private static final String TAG = "ResultActivity";

    private View progressBar;
    private TextView resultText;
    private ImageView resultImage;
    private View tryAgainContainer;
    private View horizontalSeparator;
    private TextView tryAgain;
    private TextView backMenu;
    private int faceNaoIdentificadaDocumento = 400;
    public static String VIDEO_MP_4;
    public static final int SERVER_TIMEOUT = 60000;
    public static final int ENTER_ANIMATION_DURATION = 200;
    private File video;
    private static String serverUrl;
    private FragmentManager fragmentManager;
    private Slide enterAnimation;
    private AnvSurfaceType anvSurfaceType;
    private int usuarioJaCadastrado = 409;
    private TextView anyvisionUrl;
    private static StringsFirebase str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        progressBar = findViewById(R.id.progress_bar);
        resultText = findViewById(R.id.result_text);
        resultImage = findViewById(R.id.result_img);
        tryAgainContainer = findViewById(R.id.try_again_container);
        horizontalSeparator = findViewById(R.id.horizontal_separator);
        tryAgain = findViewById(R.id.try_again);
        backMenu = findViewById(R.id.menu);
        VIDEO_MP_4 = getString(R.string.sesameVideoMp4);
        fragmentManager = getSupportFragmentManager();
        enterAnimation = new Slide(Gravity.BOTTOM);
        enterAnimation.setDuration(ENTER_ANIMATION_DURATION);
        video = new File(getExternalFilesDir(null) + VIDEO_MP_4);

        serverUrl = GetVariables.getInstance().getEtAnyvisionUrl();
        if(serverUrl == null)
            serverUrl = getString(R.string.servidorSesame);

        anvSurfaceType = AnvSurfaceType.DarkSurface;
        AppData.setVideo(video);
        View tryAgain = findViewById(R.id.try_again);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoActivity.startActivity(view.getContext());
                finish();
            }
        });
        matchPhotos();

        backMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.getInstance().finish();
                InstructionsActivity.getInstance().finish();
                LoginActivity.startActivity(ResultActivity.this);
                finish();
            }
        });
    }

    private void matchPhotos() {
        resultText.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        tryAgainContainer.setVisibility(View.GONE);
        horizontalSeparator.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String userId = InfoMobile.getMacAddress() + "/" + GetVariables.getInstance().getEtRegisterUsername();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startLivenessResults(AppData.getIdImg(), AppData.getVideo(), userId);
    }

    private void startLivenessResults(File imageFile, File videoFile, String userId) {
        Sesame.initialize(serverUrl, SERVER_TIMEOUT);
        registerToSesame(imageFile, videoFile, userId);
    }

    private void registerToSesame(File imageFile, File videoFile, String userId) {
        Sesame.registerUser(imageFile, videoFile, userId, new IRegisterListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess() {

                final String userNameRegister = GetVariables.getInstance().getEtRegisterUsername();
                final String nameRegister = GetVariables.getInstance().getNameRegister();
                final String cargoRegister = GetVariables.getInstance().getCargoRegister();
                final String tipoAgencia = GetVariables.getInstance().getLocalRegister();

                if(cargoRegister.contains("Regional")){
                    SetTipoAgenciaRegionalSharedPrivate("REGIONAL");
                }
                else{
                    SetTipoAgenciaRegionalSharedPrivate("AGENCIA");
                }
                Firebase.getInstance().createUser(userNameRegister,
                        nameRegister,
                        cargoRegister,
                        tipoAgencia);

                String mac = InfoMobile.getMacAddress();
                Firebase.unRegister(mac.replace(str.caracterDoisPontos, ""), true);
                onResult(true, getString(R.string.result_success));
            }

            @Override
            public void onFailure(int errorCode) {
                if (errorCode == faceNaoIdentificadaDocumento) {
                    onResult(false, getString(R.string.faceNaoIdentificadaNoDocumento));
                } else if (errorCode == usuarioJaCadastrado) {
                    onResult(false, getString(R.string.usuarioJaCadastrado));
                } else {
                    onResult(false, getString(R.string.falhaNaVerificacao));
                }
            }
        });
    }

    private void onResult(boolean isSuccess, String msg) {
        resultText.setVisibility(View.VISIBLE);
        resultImage.setVisibility(View.VISIBLE);
        horizontalSeparator.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        resultText.setText(msg);
        if (isSuccess) {
            resultImage.setImageDrawable(getDrawable(R.drawable.success));
            tryAgainContainer.setVisibility(View.VISIBLE);
            backMenu.setVisibility(View.VISIBLE);
            tryAgain.setVisibility(View.GONE);
        } else {
            resultImage.setImageDrawable(getDrawable(R.drawable.failure));
            tryAgainContainer.setVisibility(View.VISIBLE);
            tryAgain.setVisibility(View.VISIBLE);
            backMenu.setVisibility(View.GONE);
        }
        File videoAndImageDir = AppData.getVideo().getParentFile();
        videoAndImageDir.delete();
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, ResultActivity.class);
        from.startActivity(intent);
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void switchFragments(eFragmentChooser eFragmentChooser) {
    }

    @Override
    public void goBackFromFragment() {
    }

    private void  SetTipoAgenciaRegionalSharedPrivate(String tipo){
        try {
            SharedPreferences shTipoAgenciaRegional = getSharedPreferences(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), MODE_PRIVATE);
            SharedPreferences.Editor editTipoAgReg = shTipoAgenciaRegional.edit();
            editTipoAgReg.clear().commit();
            editTipoAgReg.putString(Enum.SharedPrivate.TIPO_AGENCIA_REGIONAL.toString(), tipo);
            editTipoAgReg.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

