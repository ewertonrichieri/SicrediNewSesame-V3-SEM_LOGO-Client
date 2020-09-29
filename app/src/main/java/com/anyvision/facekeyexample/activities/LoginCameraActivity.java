package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import androidx.fragment.app.FragmentManager;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.sesame.Sesame;
import com.anyvision.sesame.fragments.LivenessFragment;
import com.anyvision.sesame.listeners.FragmentCommunicatior;
import com.anyvision.sesame.listeners.eFragmentChooser;
import com.anyvision.sesame.utils.AnvSurfaceType;
import java.io.File;

public class LoginCameraActivity extends BaseActivity implements FragmentCommunicatior {

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
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_enrollment);
            fragmentManager = getSupportFragmentManager();
            enterAnimation = new Slide(Gravity.BOTTOM);
            enterAnimation.setDuration(ENTER_ANIMATION_DURATION);
            video = new File(getExternalFilesDir(null) + VIDEO_MP_4);
            AppData.setVideo(video);

            serverUrl = GetVariables.getInstance().getEtAnyvisionUrl();
            if (serverUrl == null)
                serverUrl = getString(R.string.servidorSesame);

            anvSurfaceType = AnvSurfaceType.DarkSurface;
            btnBack = findViewById(R.id.livenessBackBtn);
            imageFile = AppData.getIdImg();

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFullScreen() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void switchFragments(eFragmentChooser fragmentChooser) {
        clearBackStack();
    }

    private void startLiveness() {

        Sesame.initialize(serverUrl, SERVER_TIMEOUT);

        if (anvSurfaceType == null)
            anvSurfaceType = AnvSurfaceType.DarkSurface;

        final boolean useOpacity = true;
        final boolean useTutorial = false; //set to true if you want help buttons to be visible in screen
        LivenessFragment livenessFragment = LivenessFragment.newInstance(video.getAbsolutePath(), useOpacity, false, useTutorial, true, anvSurfaceType);
        livenessFragment.setEnterTransition(enterAnimation);
        livenessFragment.setAllowEnterTransitionOverlap(true);
        livenessFragment.setiLivenessFragmentListener(new LivenessFragment.ILivenessFragmentListener() {

            @Override
            public void showLivenessTutorial() {

            }

            @Override
            public void showLivenessResults() {//Liveness process completed. video is ready to use.

                LoginResultActivity.startActivity(LoginCameraActivity.this);
                finish();
            }
        });
        fragmentManager.beginTransaction().replace(R.id.fragmentsHolder, livenessFragment).addToBackStack(livenessFragment.getTag()).commit();
    }

    @Override
    public void goBackFromFragment() {

        LoginActivity.startActivity(LoginCameraActivity.this);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLiveness();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, LoginCameraActivity.class);
        from.startActivity(intent);
    }

    public void onBackPressed() {
        LoginActivity.startActivity(LoginCameraActivity.this);
        finish();
    }
}