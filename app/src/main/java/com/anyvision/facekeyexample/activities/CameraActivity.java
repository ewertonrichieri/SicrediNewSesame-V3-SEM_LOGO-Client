package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.sesame.fragments.LivenessFragment;
import com.anyvision.sesame.listeners.FragmentCommunicatior;
import com.anyvision.sesame.listeners.eFragmentChooser;
import com.anyvision.sesame.utils.AnvSurfaceType;
import java.io.File;

public class CameraActivity extends AppCompatActivity implements FragmentCommunicatior {

    public static final String VIDEO_MP_4 = "/SesameVideo.mp4";
    public static final int ENTER_ANIMATION_DURATION = 200;
    private File imageFile;
    private File video;
    private String userId;
    private String serverUrl;
    private FragmentManager fragmentManager;
    private Slide enterAnimation;
    private AnvSurfaceType anvSurfaceType;
//    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_enrollment);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            fragmentManager = getSupportFragmentManager();
            enterAnimation = new Slide(Gravity.BOTTOM);
            enterAnimation.setDuration(ENTER_ANIMATION_DURATION);
            video = new File(getExternalFilesDir(null) + VIDEO_MP_4);
            userId = InfoMobile.getMacAddress();
            Log.d("infoIdMac", InfoMobile.getMacAddress());
            AppData.setVideo(video);
//            serverUrl = "http://emea-sesame.anyvision.co:3003";
            serverUrl = "http://200.188.213.130:3003";
            anvSurfaceType = AnvSurfaceType.DarkSurface;
//            btnBack = findViewById(R.id.livenessBackBtn);
            imageFile = AppData.getIdImg();

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
        //Esta função só serve se for usar a interface do sdk sesame
//        Log.d("fragmentEscolha", "getBackStackEntryCount = " + String.valueOf(fragmentManager.getBackStackEntryCount()));
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(0);
//            fragmentManager.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }
    }

    @Override
    public void switchFragments(eFragmentChooser fragmentChooser) {
        //Esta função só serve se for usar a interface do sdk sesame
//        if (fragmentChooser == eFragmentChooser.clearBackStack) {
//        clearBackStack();
        //}
    }

    private void startLiveness() {

        if (anvSurfaceType == null)
            anvSurfaceType = AnvSurfaceType.DarkSurface;

        final boolean useOpacity = true;
        final boolean useTutorial = false; //set to true if you want help buttons to be visible in screen
        boolean detectBackLight = false;
        boolean showLivenessAnimations = true;
        LivenessFragment livenessFragment = LivenessFragment.newInstance(video.getAbsolutePath(), useOpacity, detectBackLight, useTutorial, showLivenessAnimations, anvSurfaceType);
        livenessFragment.setEnterTransition(enterAnimation);
        livenessFragment.setAllowEnterTransitionOverlap(true);
        livenessFragment.setiLivenessFragmentListener(new LivenessFragment.ILivenessFragmentListener() {

            @Override
            public void showLivenessTutorial() {
                //show your tutorial page
            }

            @Override
            public void showLivenessResults() {//Liveness process completed. video is ready to use.

                ResultActivity.startActivity(CameraActivity.this);
            }
        });
        fragmentManager.beginTransaction().replace(R.id.fragmentsHolder, livenessFragment).addToBackStack(livenessFragment.getTag()).commit();
    }

    protected void onResume() {
        super.onResume();
        setFullScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLiveness();
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
        Intent intent = new Intent(from, CameraActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void goBackFromFragment() {

        PhotoActivity.startActivity(CameraActivity.this);
        finish();
    }

    public void onBackPressed(){
        PhotoActivity.startActivity(CameraActivity.this);
        finish();
    }
}
