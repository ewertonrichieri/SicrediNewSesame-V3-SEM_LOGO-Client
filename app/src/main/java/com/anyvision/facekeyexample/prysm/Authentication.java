package com.anyvision.facekeyexample.prysm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anyvision.facekeyexample.FacekeyApplication;
import com.anyvision.facekeyexample.activities.logged.SolicitationExtensionActivity;
import com.anyvision.facekeyexample.activities.logged.SolicitationHistoryApproved;
import com.anyvision.facekeyexample.activities.logged.SolicitationHistoryReproved;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.MessageTopic;
import com.anyvision.facekeyexample.models.SolicitationExtension;
import com.anyvision.facekeyexample.models.VariableRow;
import com.anyvision.facekeyexample.models.VariableRowChamado;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Authentication extends Application {
    private String serverLocalUrl;
    private boolean statusServer;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public Authentication(String serverLocalUrl) {
        this.serverLocalUrl = serverLocalUrl;
        this.statusServer = false;
        try {
            verifyServerStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestToken(final String name, final String newValue) {

        final String AccountType = GetVariables.getInstance().getSpTypeAccount();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        final AuthToken tokenAuth = retrofit.create(AuthToken.class);
        Call<ResponseBody> call = tokenAuth.getToken();

        call.enqueue(new Callback<ResponseBody>() {
            private String token;
            private String hashpassword;

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    token = cleanOUTPUT(response.body().string());
                    hashpassword = md5(token + md5(AccountType + AccountType));
                    if (response.isSuccessful()) {

                        if (getContext() == null) {
                            mContext = FacekeyApplication.getAppContext();
                        }

                        if (name == "aprovaReprovaExtesao") {

                            if (newValue.equals("geral")) {
                                GetVariableSolicitationExtensionGeral(token);
                            }

                            //pega a lista dos estados aprovados ou reprovados das solicitações enviadas pelas agencias
                            if (newValue == "state")
                                GetVariableStateSolicitHistory(token);

                            if (newValue == "descriptions")
                                GetVariablesByFilterDescription(token);

                            if (newValue == "solicitationExtension")
                                GetVariableSolicitationExtension(token);

                            if (newValue == "chamadoDescriptionsButtons")
                                GetVariableDescripBtnChamado(token);

                            if (newValue == "FirebaseNotification")
                                NotifFirebaseSolicitation(token);
                        }

                        if (name != "aprovaReprovaExtesao")
                            getAuthentication(token, hashpassword, name, newValue);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ErroConexao", t.getMessage());
            }
        });
    }

    private void getAuthentication(final String SessionId, String pass, final String name, final String newValue) {

        final String AccountType = GetVariables.getInstance().getSpTypeAccount();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Log.d("auth", pass);
        Call<Void> call = tokenAuth.signIn(SessionId, AccountType, pass);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("auth", "Passou in getAuthentication");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());

                    setVariable(SessionId, name, newValue);
                } else {
                    Log.d("ErroConexao", "FECHOU E ABRIU DENOVO in getAuthentication");
                    Log.d("ErroConexao", String.valueOf(response.code()));
                    Log.d("ErroConexao", response.toString());
                    if (response.code() == 402)
                        showToast(response.code());

                    closeSession(SessionId);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("ErroConexao", t.getMessage());
            }
        });
    }

    private void setVariable(final String SessionId, final String name, final String newValue) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<Void> call = tokenAuth.setVariable(SessionId, name, newValue);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("chamado", "acho q setou agora a variavel");

                showToast(response.code());
                if (response.isSuccessful()) {
                    Log.d("chamado", "SETOU SIM");

                    if (MessageTopic.getMessage() != null && MessageTopic.getTitle() != null && MessageTopic.getTitle() != null)
                        iniciarNotificacao(MessageTopic.getTopic(), MessageTopic.getTitle(), MessageTopic.getMessage());

                    if (name.contains(".Reprogramacao") && SolicitationExtensionActivity.onActive()) {

                        GetVariableStateSolicitHistory(SessionId);
                    } else {
                        closeSession(SessionId);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Log.d("ErroConexao", t.getMessage());
                closeSession(SessionId);
            }
        });
    }

    public void setServerLocalUrl(String serverLocalUrl) {
        this.serverLocalUrl = serverLocalUrl;
    }

    private void iniciarNotificacao(String typeAgencia, String title, String mensagem) {
        JSONObject notificacao = new JSONObject();
        JSONObject dados = new JSONObject();

        try {
            notificacao.put("to", "/topics/" + typeAgencia);
            dados.put("titulo", title + " ");
            dados.put("mensagem", mensagem);

            notificacao.put("data", dados);
            String site = "https://fcm.googleapis.com/fcm/send";
            enviarNotificacao(site, notificacao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarNotificacao(String site, JSONObject notificacao) {

        try {
            Context context = getContext();
            if (context == null) {

                mContext = FacekeyApplication.getAppContext();
            }

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, site, notificacao,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("mensagem_Firebase", "mensagem enviada OK");
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("mensagem_Firebase", error.getMessage());
                        }

                    }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();

                    header.put("Authorization", "key=AAAA-XxXKBU:APA91bE2OVsZcY1aVyMJ5q2riSJN6Tfr1uC6vBxFpHpDHn8CJ7fVWc-h_2iw8KnwPbcIBkAystkOiTlV_58Yo-XseHZfHQrW1MYTt7CuRVnyhVzKwRiK9gEGK0ynkt64NlexORLzMPQP");
                    header.put("Content-Type", "application/json");
                    return header;
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy(10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetVariablesByFilterDescription(final String SessionID) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<VariableRow> call = tokenAuth.GetVariableRow(SessionID);

        call.enqueue(new Callback<VariableRow>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<VariableRow> call, Response<VariableRow> response) {

                if (response.isSuccessful()) {
                    VariableRow desc = response.body();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear().commit();

                    ArrayList<String> listaDescriptions = desc.getListaDescription();

                    editor.putInt("descriptions_size", listaDescriptions.size());
                    for (int i = 0; i < desc.getListaDescription().size(); i++) {
                        editor.putString("descriptions" + "_" + i, listaDescriptions.get(i));
                    }
                    editor.apply();

                    Log.d("auth", response.toString());
                    Log.d("auth", response.message());
                    assert response.body() != null;
                    Log.d("auth", response.body().toString());
                    closeSession(SessionID);

                } else {
                    assert response.errorBody() != null;
                    Log.d("auth", response.errorBody().toString());
                    closeSession(SessionID);
                }
            }

            @Override
            public void onFailure(Call<VariableRow> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    public void GetVariableSolicitationExtension(final String SessionID) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<SolicitationExtension> call = tokenAuth.GetSolicitationExtension(SessionID);

        call.enqueue(new Callback<SolicitationExtension>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SolicitationExtension> call, Response<SolicitationExtension> response) {

                Log.d("403 error bateu", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    SolicitationExtension solicitation = response.body();

                    assert response.body() != null;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    ArrayList<String> listaSolicitation = solicitation.getlistaNames();

                    editor.putInt("solicitacao_size", listaSolicitation.size());
                    for (int i = 0; i < solicitation.getlistaNames().size(); i++)
                        editor.putString("solicitacao" + "_" + i, listaSolicitation.get(i));
                    editor.commit();

                    SolicitationExtensionActivity.setAllowGetlistSolicitation();
                    Log.d("naoBate", "Bateu");
                    closeSession(SessionID);

                } else {
                    assert response.errorBody() != null;
                    Log.d("auth", response.errorBody().toString());
                    if (response.code() == 403)
                        requestToken("aprovaReprovaExtesao", "solicitationExtension");

                    closeSession(SessionID);
                }
            }

            @Override
            public void onFailure(Call<SolicitationExtension> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    String listaWithReplace;

    public void GetVariableStateSolicitHistory(final String SessionID) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<SolicitationExtension> call = tokenAuth.GetSolicitHistApprovedReproved(SessionID);

        call.enqueue(new Callback<SolicitationExtension>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SolicitationExtension> call, Response<SolicitationExtension> response) {

                if (response.isSuccessful()) {
                    SolicitationExtension solicitation = response.body();

                    assert response.body() != null;
                    Log.d("auth", response.body().toString());
                    closeSession(SessionID);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Lista de solicitações aprovadas pelo Gerente Regional;
                    if (solicitation.getListSolicitHistApproved().size() > 0) {

                        ArrayList<String> listaSolicitHistAprovado = solicitation.getListSolicitHistApproved();

                        editor.putInt("solicitacaoAprovada_size", listaSolicitHistAprovado.size());
                        for (int i = 0; i < solicitation.getListSolicitHistApproved().size(); i++) {
                            listaWithReplace = listaSolicitHistAprovado.get(i).replace("App.REGIONAL.POC.", "");
                            listaWithReplace = listaWithReplace.replace(".Reprogramacao", "");

                            editor.putString("solicitacaoAprovada" + "_" + i, listaWithReplace);
                        }

                        editor.commit();

                        if (listaSolicitHistAprovado.size() > 0) {
                            SolicitationHistoryApproved.setAllowGetlistSolicitHist();
                        }
                    }

                    //Lista de solicitações reprovadas pelo Gerente Regional;
                    if (solicitation.getListSolicitHistReproved().size() > 0) {

                        ArrayList<String> listaSolicitHistReprovado = solicitation.getListSolicitHistReproved();

                        editor.putInt("solicitacaoReprovada_size", listaSolicitHistReprovado.size());
                        for (int i = 0; i < solicitation.getListSolicitHistReproved().size(); i++) {
                            listaWithReplace = listaSolicitHistReprovado.get(i).replace("App.REGIONAL.POC.", "");
                            listaWithReplace = listaWithReplace.replace(".Reprogramacao", "");

                            editor.putString("solicitacaoReprovada" + "_" + i, listaWithReplace);
                        }
                        editor.commit();

                        if (listaSolicitHistReprovado.size() > 0) {
                            SolicitationHistoryReproved.setAllowGetlistSolicitHist();
                        }
                    }

                } else {
                    assert response.errorBody() != null;
                    Log.d("auth", response.errorBody().toString());
                    closeSession(SessionID);
                }
            }

            @Override
            public void onFailure(Call<SolicitationExtension> call, Throwable t) {
                Log.d("auth", t.getMessage());
                closeSession(SessionID);

            }
        });
    }

    //GetGeral so na primeira vez
    public void GetVariableSolicitationExtensionGeral(final String SessionID) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<SolicitationExtension> call = tokenAuth.GetSolicitationExtension(SessionID);

        call.enqueue(new Callback<SolicitationExtension>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SolicitationExtension> call, Response<SolicitationExtension> response) {

                if (response.isSuccessful()) {
                    SolicitationExtension solicitation = response.body();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear().commit();

                    ArrayList<String> listaSolicitation = solicitation.getlistaNames();

                    editor.putInt("solicitacao_size", listaSolicitation.size());
                    for (int i = 0; i < solicitation.getlistaNames().size(); i++)
                        editor.putString("solicitacao" + "_" + i, listaSolicitation.get(i));
                    editor.commit();

                    GetVariableStateSolicitHistory(SessionID);
                    SolicitationExtensionActivity.setAllowGetlistSolicitation();

                } else {
                    assert response.errorBody() != null;
                    Log.d("auth", response.errorBody().toString());
                    closeSession(SessionID);
                }
            }

            @Override
            public void onFailure(Call<SolicitationExtension> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    public void GetVariableDescripBtnChamado(final String SessionID) {
        Log.d("chamadoLista", "GetVariableDescripBtnChamado");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<VariableRowChamado> call = tokenAuth.GetVariableRowChamado(SessionID);

        call.enqueue(new Callback<VariableRowChamado>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<VariableRowChamado> call, Response<VariableRowChamado> response) {

                if (response.isSuccessful()) {

                    VariableRowChamado desc = response.body();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    closeSession(SessionID);

                    ArrayList<String> listaDescriptions = desc.getListChamado();

                    editor.putInt("chamadoDescriptions_size", listaDescriptions.size());
                    for (int i = 0; i < desc.getListChamado().size(); i++) {
                        editor.putString("chamado" + "_" + i, listaDescriptions.get(i));
                        Log.d("chamadoLista", String.valueOf(i) + listaDescriptions.get(i));
                    }

                    editor.apply();

                    closeSession(SessionID);

                } else {
                    assert response.errorBody() != null;
                    Log.d("auth", response.errorBody().toString());
                    closeSession(SessionID);
                }
            }

            @Override
            public void onFailure(Call<VariableRowChamado> call1, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }


    public void NotifFirebaseSolicitation(final String SessionID) {
        Log.d("FirebaseNotify", "aguardando request do servidor");

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<SolicitationExtension> call = tokenAuth.GetSolicitationExtension(SessionID);

        call.enqueue(new Callback<SolicitationExtension>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SolicitationExtension> call, Response<SolicitationExtension> response) {

                if (response.isSuccessful()) {

                    SolicitationExtension solicitation = response.body();

                    assert response.body() != null;
                    Log.d("auth", response.body().toString());
                    //closeSession(SessionID);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    ArrayList<String> listaSolicitation = solicitation.getlistaNames();

                    editor.putInt("solicitacao_size", listaSolicitation.size());
                    for (int i = 0; i < solicitation.getlistaNames().size(); i++)
                        editor.putString("solicitacao" + "_" + i, listaSolicitation.get(i));
                    editor.commit();

                    SolicitationExtensionActivity.setAllowGetlistSolicitation();
                    SolicitationExtensionActivity.refreshActivity();
                    closeSession(SessionID);

                } else {
                    assert response.errorBody() != null;
                    Log.d("FirebaseNotify", response.errorBody().toString());
                    closeSession(SessionID);
                }
            }

            @Override
            public void onFailure(Call<SolicitationExtension> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    private void showToast(int code) {

        Context context = getContext();
        if (context == null) {
            mContext = FacekeyApplication.getAppContext();
        }

        switch (code) {

            case 200:
                Toast.makeText(getContext(), "Mensagem enviada com sucesso!", Toast.LENGTH_LONG).show();
                break;
            case 402:
                Toast.makeText(getContext(), "Sistema ocupado, tente novamente em alguns minutos.", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "Mensagem não foi enviada. Por favor, verifique o status do servidor!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void verifyServerStatus() {
        setServerLocalUrl(GetVariables.getInstance().getServerUrl());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<ResponseBody> call = tokenAuth.getServerStatus();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setStatusServer(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("erroServerAut", t.getMessage());
                setStatusServer(false);
            }
        });
    }

    public void setStatusServer(boolean status) {
        this.statusServer = status;
    }

    public boolean getStatusServer() {
        return this.statusServer;
    }

    private void closeSession(final String SessionId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<Void> call = tokenAuth.closeSession(SessionId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("auth", "close session sucess");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                } else {
                    Log.d("auth", "close session failed");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("auth", t.getMessage());

            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    public String cleanOUTPUT(String OUTPUT) {
        OUTPUT = OUTPUT.replaceAll("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "");
        OUTPUT = OUTPUT.replaceAll("</string>", "");
        return OUTPUT;
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
}
