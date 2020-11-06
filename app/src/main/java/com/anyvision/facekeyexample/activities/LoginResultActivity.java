package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.logged.MainActivity;
import com.anyvision.facekeyexample.activities.logged.SolicitationExtensionActivity;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.prysm.Authentication;
import com.anyvision.sesame.Sesame;
import com.anyvision.sesame.listeners.FragmentCommunicatior;
import com.anyvision.sesame.listeners.IAuthenticateListener;
import com.anyvision.sesame.listeners.eFragmentChooser;
import com.anyvision.sesame.utils.AnvSurfaceType;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.File;
import java.text.SimpleDateFormat;

public class LoginResultActivity extends BaseActivity implements FragmentCommunicatior {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");

    private View progressBar;
    private TextView resultText;
    private ImageView resultImage;
    private View tryAgainContainer;
    private View horizontalSeparator;
    private Authentication auth;
    private String typeAccount;

    public static final String VIDEO_MP_4 = "/SesameVideo.mp4";
    public static final int PERMISSIONS_CODE = 100;
    public static final int SERVER_TIMEOUT = 60000;
    public static final int ENTER_ANIMATION_DURATION = 200;
    public static final int ANIMATION_DELAY = 50;
    public static final int SOMETHING_WENT_WRONG = 501;
    private File imageFile;
    private File video;
    private String serverUrl;
    private FragmentManager fragmentManager;
    private Slide enterAnimation;
    private AnvSurfaceType anvSurfaceType;
    private static String etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);
        progressBar = findViewById(R.id.progress_bar);
        resultText = findViewById(R.id.result_text);
        resultImage = findViewById(R.id.result_img);
        tryAgainContainer = findViewById(R.id.try_again_container);
        horizontalSeparator = findViewById(R.id.horizontal_separator);
        fragmentManager = getSupportFragmentManager();
        enterAnimation = new Slide(Gravity.BOTTOM);
        enterAnimation.setDuration(ENTER_ANIMATION_DURATION);
        video = new File(getExternalFilesDir(null) + VIDEO_MP_4);
        AppData.setVideo(video);
        etUsername = GetVariables.getInstance().getEtUsername();

        serverUrl = GetVariables.getInstance().getEtAnyvisionUrl();
        if(serverUrl == null)
            serverUrl = getString(R.string.servidorSesame);

        anvSurfaceType = AnvSurfaceType.DarkSurface;

        authenticate();

        typeAccount = GetVariables.getInstance().getSpTypeAccount();
        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        tryAgainContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginCameraActivity.startActivity(LoginResultActivity.this);
            }
        });
    }

    private void authenticate() {
        resultText.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        tryAgainContainer.setVisibility(View.GONE);
        horizontalSeparator.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String userId = InfoMobile.getMacAddress() + "/" + GetVariables.getInstance().getEtUsername();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        authenticateWithSesame(video, userId);
    }

        private void authenticateWithSesame(File video, String userId){
            Sesame.initialize(serverUrl, SERVER_TIMEOUT);
            Sesame.authenticateUser(video, userId, new IAuthenticateListener() {

                @Override
                public void onSuccess() {
                    if (typeAccount.equals("REGIONAL")) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("AGENCIA");
                        FirebaseMessaging.getInstance().subscribeToTopic("REGIONAL");

                        auth.requestToken("aprovaReprovaExtesao", "geral");
                        SolicitationExtensionActivity.startActivity(LoginResultActivity.this);
                    } else {
                        MainActivity.startActivity(LoginResultActivity.this);
                    }
                    finish();
                }

                @Override
                public void onFailure(int errorCode) {
                    onResult(false, "Usuário " +etUsername+ " não identificado! Por favor tente novamente");
                }
            });
        }

    private void onResult(boolean isSuccess, String msg) {
        resultText.setVisibility(View.VISIBLE);
        resultImage.setVisibility(View.VISIBLE);
        tryAgainContainer.setVisibility(View.VISIBLE);
        horizontalSeparator.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        resultText.setText(msg);
        if (isSuccess) {
            resultImage.setImageDrawable(getDrawable(R.drawable.success));
        } else {
            resultImage.setImageDrawable(getDrawable(R.drawable.failure));
        }
        File videoAndImageDir = AppData.getVideo().getParentFile();
        AppData.getVideo().delete();
        videoAndImageDir.delete();
    }

    @Override
    public void switchFragments(eFragmentChooser eFragmentChooser) {

    }

    @Override
    public void goBackFromFragment() {

    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, LoginResultActivity.class);
        from.startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed(){
     LoginCameraActivity.startActivity(LoginResultActivity.this);
     finish();
    }
}
