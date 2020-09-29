package com.anyvision.facekeyexample.firebase;

import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Firebase {
    private static DatabaseReference mDatabase;
    private static Firebase firebaseInstance;
    private String mac;
    private static StringsFirebase str;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(str.dataHoraAtual);
    private static LocalDateTime localDateTime;
    private static String dataHoraNaoRegistrado;

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

    public static void getTypeFirebase() {
        try {
            String mac = InfoMobile.getMacAddress();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(str.typeComBarra + mac + str.tipoComBara);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String post = String.valueOf(dataSnapshot.getValue());
                    GetVariables.getInstance().setSpTypeAccount(post);
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

    public static void unRegister(String mac, Boolean registro) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            dataHoraNaoRegistrado = dataHoraAtual();
            String modeloMobile = InfoMobile.getModeloMobile();

            if (registro) {
                mDatabase.child(str.unRegister).child(str.caracterPorcentagem + mac).removeValue();
                LoginActivity.stopThreadLogin();
            } else {
                mDatabase.child(str.unRegister).child(str.caracterPorcentagem + mac).child(modeloMobile).setValue(dataHoraNaoRegistrado + " -> " + str.naoRegistrado);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerType(String mac, String tipo) {
        try {
            mDatabase.child(str.type).child(mac)
                    .child(str.tipo).setValue(tipo);
        } catch (Exception e) {
        }
    }

    public static String dataHoraAtual(){
        localDateTime = LocalDateTime.now();
        dataHoraNaoRegistrado = formatter.format(localDateTime);
        return dataHoraNaoRegistrado;
    }

    public void createUser(String username,
                           String name,
                           String cargo,
                           String agencia) {

        String mac = InfoMobile.getMacAddress();
        String key = mac + str.caracterBarra + username;
        String dtRegistro = dataHoraAtual();
        String modeloMobile = InfoMobile.getModeloMobile();

        mDatabase.child(str.users).child(key)
                .child(str.username).setValue(username);

        mDatabase.child(str.users).child(key)
                .child(str.name).setValue(name);

        mDatabase.child(str.users).child(key)
                .child(str.macAddress).setValue(mac);

        mDatabase.child(str.users).child(key)
                .child(str.cargo).setValue(cargo);

        mDatabase.child(str.users).child(key)
                .child(str.agencia).setValue(agencia);

        mDatabase.child(str.users).child(key)
                .child(str.dtRegistro).setValue(dtRegistro);

        mDatabase.child(str.users).child(key)
                .child(str.modelo).setValue(modeloMobile);

        if (cargo.equals(str.gerenteAgencia)) {
            mDatabase.child(str.users).child(key)
                    .child(str.tipo).setValue(str.AGENCIA);

            registerType(mac, str.AGENCIA);

            GetVariables.getInstance().setSpTypeAccount(str.AGENCIA);
            GetVariables.getInstance().setNameAgencia(str.App_AGENCIA_POC_AGENCIA0001);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(str.REGIONAL);

        } else if (cargo.equals(str.gerenteRegional)) {
            mDatabase.child(str.users).child(key)
                    .child(str.tipo).setValue(str.REGIONAL);
            GetVariables.getInstance().setSpTypeAccount(str.REGIONAL);
            GetVariables.getInstance().setNameAgencia(str.App_REGIONAL_POC_AGENCIA0001_Reprogramacao);

            registerType(mac, str.REGIONAL);

            FirebaseMessaging.getInstance().subscribeToTopic(str.REGIONAL);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(str.AGENCIA);
        }

        mDatabase.child(str.macs).child(mac)
                .child(username).setValue("1");
    }

    public void sendNotification(boolean status, String username) {
        try {
            String mac = InfoMobile.getMacAddress();
            String key = mac + str.caracterBarra + username;

            mDatabase.child(str.notification).child(key)
                    .child(str.username).setValue(username);

            mDatabase.child(str.notification).child(key)
                    .child(str.solicitacaoExtensao).setValue(status);

            mDatabase.child(str.notification).child(key)
                    .child(str.dataEnvioMessagem).setValue(dataHoraAtual());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTokenFirebaseToServer(String token) {

        try {
            Log.d("token", token);
            mDatabase.child(str.users).child(this.mac)
                    .child(str.tokenNotification).setValue(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
