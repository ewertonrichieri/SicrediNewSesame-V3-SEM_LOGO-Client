package com.anyvision.facekeyexample.activities.login;

public class ListaNewValueName {

    static public String Name;
    static public String NewValue;


    public ListaNewValueName(){}

    public ListaNewValueName(String name, String newValue){
        this.Name = name;
        this.NewValue = newValue;

    }

    public static String getName(){
        return Name;
    }

    public static String getNewValue(){
        return NewValue;
    }


}
