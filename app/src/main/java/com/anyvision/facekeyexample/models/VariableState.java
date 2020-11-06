package com.anyvision.facekeyexample.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "VariableState", strict = false)
public class VariableState {

    //teste
    @Element(name = "Parameters", required = false)
    private String Parameters;

    @Element(name = "IsLocked", required = false)
    private String IsLocked;

    @Element(name = "Severity", required = false)
    private String Severity;

    @Element(name = "Operation", required = false)
    private String Operation;

    @Element(name = "Info", required = false)
    private String Info;

    @Element(name = "OriginDesc", required = false)
    private String OriginDesc;

//    @Element(name = "PreviousValue", required = false)
//    private PreviousValueTeste PreviousValue;

    @Element(name = "OriginId", required = false)
    private String OriginId;

    @Element(name = "Date", required = false)
    private String Date;

    @Element(name = "PreviousDate", required = false)
    private String PreviousDate;

    @Element(name = "Name", required = false)
    private String Name;

    @Element(name = "IsAlarmMasked", required = false)
    private String IsAlarmMasked;

    @Element(name = "Quality", required = false)
    private String Quality;

    @Element(name = "ValueInternal", required = false)
    private String ValueInternal;

    @Element(name = "Value", required = false)
    private String Value;

    @Element(name = "Id", required = false)
    private String Id;

    public String GetValor(){
        return Value;
    }

    public String GetName(){
        return Name;
    }

//    //Recupera as solicitações que as agencias solicitaram
//    public ArrayList<String> GetListaGestaoControleSala() {
//        ArrayList<String> listaGestaoCtr = new ArrayList<String>();
//
//        if (name1 != null)
//            listaGestaoCtr.add(name1 + ";" + value1);
//        listaGestaoCtr.add(name2 + ";" + value2);
//        listaGestaoCtr.add(name3 + ";" + value3);
//
//        return listaGestaoCtr;
//    }
//
//    //Pega a soma dos valores para ser apresentado no grafico
//    public int GetPorcentagemTotalGestaoControleSala() {
//        int soma = 0;
//        soma = value1 + value2 + value3;
//        return soma;
//    }
}
