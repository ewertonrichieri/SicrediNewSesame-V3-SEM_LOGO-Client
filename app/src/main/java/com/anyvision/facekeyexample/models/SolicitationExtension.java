package com.anyvision.facekeyexample.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(strict = false)
public class SolicitationExtension {

    @Element(name = "Name", required = false)
    @Path("VariableState[1]")
    private String name1;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[1]")
    private int value1;

    @Element(name = "Name", required = false)
    @Path("VariableState[2]")
    private String name2;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[2]")
    private int value2;

    @Element(name = "Name", required = false)
    @Path("VariableState[3]")
    private String name3;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[3]")
    private int value3;

    @Element(name = "Name", required = false)
    @Path("VariableState[4]")
    private String name4;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[4]")
    private int value4;

    @Element(name = "Name", required = false)
    @Path("VariableState[5]")
    private String name5;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[5]")
    private int value5;

    @Element(name = "Name", required = false)
    @Path("VariableState[6]")
    private String name6;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[6]")
    private int value6;

    @Element(name = "Name", required = false)
    @Path("VariableState[7]")
    private String name7;
    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[7]")
    private int value7;

    @Element(name = "Name", required = false)
    @Path("VariableState[8]")
    private String name8;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[8]")
    private int value8;

    @Element(name = "Name", required = false)
    @Path("VariableState[9]")
    private String name9;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[9]")
    private int value9;

    @Element(name = "Name", required = false)
    @Path("VariableState[10]")
    private String name10;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[10]")
    private int value10;

    @Element(name = "Name", required = false)
    @Path("VariableState[11]")
    private String name11;

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema")
    @Element(name = "Value", required = false)
    @Path("VariableState[11]")
    private int value11;

    public int getValue1() { return value1; }
    public int getValue2() { return value2; }
    public int getValue3() { return value3; }
    public int getValue4() { return value4; }
    public int getValue5() { return value5; }
    public int getValue6() { return value6; }
    public int getValue7() { return value7; }
    public int getValue8() { return value8; }
    public int getValue9() { return value9; }
    public int getValue10() { return value10; }
    //public int getValue11() { return value11; }

    //nomes das agencias
    public String getName1() { return name1; }
    public String getName2() { return name2; }
    public String getName3() { return name3; }
    public String getName4() { return name4; }
    public String getName5() { return name5; }
    public String getName6() { return name6; }
    public String getName7() { return name7; }
    public String getName8() { return name8; }
    public String getName9() { return name9; }
    public String getName10() { return name10; }
    //public String getName11() { return name11; }

    //Recupera as solicitações que as agencias solicitaram
    public ArrayList<String> getlistaNames() {
        ArrayList<String> listaNames = new ArrayList<String>();

        if(value1 == 1) listaNames.add(name1);
        if(value2 == 1) listaNames.add(name2);
        if(value3 == 1) listaNames.add(name3);
        if(value4 == 1) listaNames.add(name4);
        if(value5 == 1) listaNames.add(name5);
        if(value6 == 1) listaNames.add(name6);
        if(value7 == 1) listaNames.add(name7);
        if(value8 == 1) listaNames.add(name8);
        if(value9 == 1) listaNames.add(name9);
        if(value10 == 1) listaNames.add(name10);
        //ignora o getValue11 porque é o nome de uma classe
        //if(getValue11() == 1) listaNames.add(name11);

        return listaNames;
    }

    //Se for aprovado value = 1
    public ArrayList<String> getListSolicitHistApproved(){
        ArrayList<String> listSolicitHistApproved = new ArrayList<String>();

        if(value1 == 1) listSolicitHistApproved.add(name1);
        if(value2 == 1) listSolicitHistApproved.add(name2);
        if(value3 == 1) listSolicitHistApproved.add(name3);
        if(value4 == 1) listSolicitHistApproved.add(name4);
        if(value5 == 1) listSolicitHistApproved.add(name5);
        if(value6 == 1) listSolicitHistApproved.add(name6);
        if(value7 == 1) listSolicitHistApproved.add(name7);
        if(value8 == 1) listSolicitHistApproved.add(name8);
        if(value9 == 1) listSolicitHistApproved.add(name9);
        if(value10 == 1) listSolicitHistApproved.add(name10);

        return listSolicitHistApproved;
    }

    //Se for reprovado value = 2
    public ArrayList<String> getListSolicitHistReproved(){
        ArrayList<String> listSolicitHistReproved  = new ArrayList<String>();

        if(value1 == 2) listSolicitHistReproved.add(name1);
        if(value2 == 2) listSolicitHistReproved.add(name2);
        if(value3 == 2) listSolicitHistReproved.add(name3);
        if(value4 == 2) listSolicitHistReproved.add(name4);
        if(value5 == 2) listSolicitHistReproved.add(name5);
        if(value6 == 2) listSolicitHistReproved.add(name6);
        if(value7 == 2) listSolicitHistReproved.add(name7);
        if(value8 == 2) listSolicitHistReproved.add(name8);
        if(value9 == 2) listSolicitHistReproved.add(name9);
        if(value10 == 2) listSolicitHistReproved.add(name10);

        return listSolicitHistReproved;
    }
}
