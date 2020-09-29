package com.anyvision.facekeyexample.models;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;
import com.anyvision.facekeyexample.prysm.Authentication;

public class GetVariables {

    private static String localServerUrl;
    private static GetVariables getVariablesInstance;
    private static TextView etLocalServerUrl;
    private static TextView textviewAnyvision;
    private static String etAnyvisionUrl;
    private static String etUsername;
    private static String etRegisterUsername;
    private static String spTypeAccount;
    private static String nameAgencia;
    private Authentication auth;
    private static String etRegisterName;
    private static String etRegisterCargo;
    private static String etRegisterLocalAgencia;

    private GetVariables(){
        localServerUrl = "";
    }

    public static synchronized GetVariables getInstance(){
        if(getVariablesInstance == null){
            getVariablesInstance = new GetVariables();
        }

        return getVariablesInstance;
    }

    public void setServerUrl(String url){
        localServerUrl = url;
    }

    public String getServerUrl(){
        return localServerUrl;
    }

    public TextView getEtLocalServerUrl() {
        return etLocalServerUrl;
    }

    public void setEtLocalServerUrl(TextView etLocalServerUrl) {
        GetVariables.etLocalServerUrl = etLocalServerUrl;
    }

    public String getEtUsername() {
        return etUsername;
    }

    public void setEtUsername(String etUsername) {
        GetVariables.etUsername = etUsername;
    }

    public String getEtRegisterUsername() {
        return etRegisterUsername;
    }

    public void setEtRegisterUsername(String etRegisterUsername) {
        GetVariables.etRegisterUsername = etRegisterUsername;
    }

    //teste
    public void setNameRegister(String nameRegister){
        GetVariables.etRegisterName = nameRegister;
    }

    public String getNameRegister(){
        return etRegisterName;
    }

    public void setCargoRegister(String cargoRegister){
        GetVariables.etRegisterCargo = cargoRegister;
    }

    public String getCargoRegister(){
        return etRegisterCargo;
    }

    public void setLocalAgenciaRegister(String agenciaLocalRegister){
        GetVariables.etRegisterLocalAgencia = agenciaLocalRegister;
    }

    public String getLocalRegister(){
        return etRegisterLocalAgencia;
    }

    public String getSpTypeAccount() {
        return spTypeAccount;
    }

    public void setSpTypeAccount(String spTypeAccount) {
        GetVariables.spTypeAccount = spTypeAccount;
    }

    public String getEtAnyvisionUrl() {
        return etAnyvisionUrl;
    }

    public void setEtAnyvisionUrl(String etAnyvisionUrl) {
        GetVariables.etAnyvisionUrl = etAnyvisionUrl;
    }

    public TextView getTextviewAnyvision() {
        return textviewAnyvision;
    }

    public void setTextviewAnyvision(TextView textviewAnyvision) {
        GetVariables.textviewAnyvision = textviewAnyvision;
    }

    public String getNameAgencia() {return nameAgencia; }

    public void setNameAgencia(String nmAgencia){
        GetVariables.nameAgencia = nameAgencia;
    }
}