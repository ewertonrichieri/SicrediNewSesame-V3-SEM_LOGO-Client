package com.anyvision.facekeyexample.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.firebase.Firebase;


public class InstructionsActivity extends AppCompatActivity {

    private static Activity finishInstructionActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        finishInstructionActivity = this;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();

        findViewById(R.id.continue_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoActivity.startActivity(InstructionsActivity.this);
            }
        });

        findViewById(R.id.back_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.startActivity(InstructionsActivity.this);
            }
        });
    }

    public static void startActivity(Context from){
        Intent intent = new Intent(from, InstructionsActivity.class);
        from.startActivity(intent);
    }

    public void onStop(){
        super.onStop();
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public static Activity getInstance(){
        return finishInstructionActivity;
    }
}
