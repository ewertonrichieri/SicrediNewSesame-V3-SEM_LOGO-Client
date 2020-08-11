package com.anyvision.facekeyexample.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anyvision.facekeyexample.FacekeyApplication;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.Context;

import com.google.gson.JsonObject;


public class Firebase {
    private static DatabaseReference mDatabase;
    private static Firebase firebaseInstance;
    private String token;
    private String mac;


    public Firebase() {
        try {
            this.mac = InfoMobile.getMacAddress();
            mDatabase = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Firebase getInstance() {

        if (firebaseInstance == null) {
            firebaseInstance = new Firebase();
        }

        return firebaseInstance;

    }

    public void allowRegisterUser() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Macs/" + mac + "/allowRegister");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post = String.valueOf(dataSnapshot.getValue());
                Log.d("data", post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("data", String.valueOf(databaseError.getCode()));
            }
        });
    }

    public static void getTypeFirebase() {
        try {
            String mac = InfoMobile.getMacAddress();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Type/" + mac + "/tipo");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String post = String.valueOf(dataSnapshot.getValue());
                    GetVariables.getInstance().setSpTypeAccount(post);
                    Log.d("dataFirebase", post);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("dataFirebase", String.valueOf(databaseError.getCode()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void unRegister(String mac, String registro) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            if (mac.contains(":")) {
                mDatabase.child("UnRegister").child("%" + mac.replace(":", "")).setValue(registro);
                LoginActivity.stopThreadLogin();
            } else {
                mDatabase.child("UnRegister").child("%" + mac).setValue(registro);
            }

            Log.d("FirebaseDeuCerto", "firebase un register ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerType(String mac, String tipo) {
        try {
            mDatabase.child("Type").child(mac)
                    .child("tipo").setValue(tipo);
        } catch (Exception e) {

        }
    }

    public void createUser(String username,
                           String name,
                           String cargo,
                           String agencia) {

        String mac = InfoMobile.getMacAddress();
        String key = mac + "/" + username;

        mDatabase.child("Users").child(key)
                .child("username").setValue(username);

        mDatabase.child("Users").child(key)
                .child("name").setValue(name);

        mDatabase.child("Users").child(key)
                .child("macAddress").setValue(mac);

        mDatabase.child("Users").child(key)
                .child("cargo").setValue(cargo);

        mDatabase.child("Users").child(key)
                .child("agencia").setValue(agencia);

        if (cargo.equals("Gerente Agência")) {
            mDatabase.child("Users").child(key)
                    .child("tipo").setValue("AGENCIA");

            registerType(mac, "AGENCIA");

            GetVariables.getInstance().setSpTypeAccount("AGENCIA");
            GetVariables.getInstance().setNameAgencia("App.AGENCIA.POC.AGENCIA0001");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("REGIONAL");

        } else if (cargo.equals("Gerente Regional")) {
            mDatabase.child("Users").child(key)
                    .child("tipo").setValue("REGIONAL");
            GetVariables.getInstance().setSpTypeAccount("REGIONAL");
            GetVariables.getInstance().setNameAgencia("App.REGIONAL.POC.AGENCIA0001.Reprogramacao");

            registerType(mac, "REGIONAL");

            FirebaseMessaging.getInstance().subscribeToTopic("REGIONAL");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("AGENCIA");
        }

        mDatabase.child("Macs").child(mac)
                .child(username).setValue("1");

        //unRegister(mac, "Registrado");
    }

    public void sendNotification(boolean status, String username) {
        try{
            String mac = InfoMobile.getMacAddress();
            String key = mac + "/" + username;

            mDatabase.child("Notification").child(key)
                    .child("username").setValue(username);

            mDatabase.child("Notification").child(key)
                    .child("SolicitacaoExtensao").setValue(status);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendTokenFirebaseToServer(String token) {

        try{
            Log.d("token", token);

            mDatabase.child("Users").child(this.mac)
                    .child("tokenNotification").setValue(token);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
