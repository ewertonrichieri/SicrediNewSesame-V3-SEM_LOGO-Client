package com.anyvision.facekeyexample.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import java.util.ArrayList;

@Root(strict = false)
public class VariableRow {

    @Element( name = "Description", required = false)
    @Path("VariableRow[1]")
    private String description1;

    @Element(name = "Description", required = false)
    @Path("VariableRow[2]")
    private String description2;

    @Element(name = "Description", required = false)
    @Path("VariableRow[3]")
    private String description3;

    @Element(name = "Description", required = false)
    @Path("VariableRow[4]")
    private String description4;

    @Element(name = "Description", required = false)
    @Path("VariableRow[5]")
    private String description5;

    @Element(name = "Description", required = false)
    @Path("VariableRow[6]")
    private String description6;

    @Element(name = "Description", required = false)
    @Path("VariableRow[7]")
    private String description7;

    @Element(name = "Description", required = false)
    @Path("VariableRow[8]")
    private String description8;

    @Element(name = "Description", required = false)
    @Path("VariableRow[9]")
    private String description9;

    @Element(name = "Description", required = false)
    @Path("VariableRow[10]")
    private String description10;

    @Element(name = "Description", required = false)
    @Path("VariableRow[11]")
    private String description11;

    @Element(name = "Description", required = false)
    @Path("VariableRow[12]")
    private String description12;

    @Element(name = "Description", required = false)
    @Path("VariableRow[13]")
    private String description13;

    @Element(name = "Description", required = false)
    @Path("VariableRow[14]")
    private String description14;

    @Element(name = "Description", required = false)
    @Path("VariableRow[15]")
    private String description15;

    public ArrayList<String> getListaDescription(){
        ArrayList<String> listaDescriptions = new ArrayList<String>();
        listaDescriptions.add(description2);
        listaDescriptions.add(description3);
        listaDescriptions.add(description4);
        listaDescriptions.add(description5);
        listaDescriptions.add(description6);
        listaDescriptions.add(description7);
        listaDescriptions.add(description15);

        return listaDescriptions;
    }

    public ArrayList<String> getListaDescriptionNameAgencia(){
        ArrayList<String> listaDescriptions = new ArrayList<String>();
        if(description1 != null) listaDescriptions.add(description1);
        if(description2 != null) listaDescriptions.add(description2);
        if(description3 != null) listaDescriptions.add(description3);
        if(description4 != null) listaDescriptions.add(description4);
        if(description5 != null) listaDescriptions.add(description5);
        if(description6 != null) listaDescriptions.add(description6);
        if(description7 != null) listaDescriptions.add(description7);
        if(description8 != null) listaDescriptions.add(description8);
        if(description9 != null) listaDescriptions.add(description9);
        if(description10 != null) listaDescriptions.add(description10);
        if(description11 != null) listaDescriptions.add(description11);

        return listaDescriptions;
    }

    @Element(name = "Name", required = false)
    @Path("VariableRow[1]")
    private String name1;

    @Element(name = "Name", required = false)
    @Path("VariableRow[2]")
    private String name2;

    @Element(name = "Name", required = false)
    @Path("VariableRow[3]")
    private String name3;

    @Element(name = "Name", required = false)
    @Path("VariableRow[4]")
    private String name4;

    @Element(name = "Name", required = false)
    @Path("VariableRow[5]")
    private String name5;

    @Element(name = "Name", required = false)
    @Path("VariableRow[6]")
    private String name6;

    @Element(name = "Name", required = false)
    @Path("VariableRow[7]")
    private String name7;

    @Element(name = "Name", required = false)
    @Path("VariableRow[8]")
    private String name8;

    @Element(name = "Name", required = false)
    @Path("VariableRow[9]")
    private String name9;

    @Element(name = "Name", required = false)
    @Path("VariableRow[10]")
    private String name10;

    @Element(name = "Name", required = false)
    @Path("VariableRow[11]")
    private String name11;

    @Element(name = "Name", required = false)
    @Path("VariableRow[12]")
    private String name12;

    @Element(name = "Name", required = false)
    @Path("VariableRow[13]")
    private String name13;

    public String getName1() { return name1; }
    public String getName2() { return name2; }
    public String getName3() { return name3; }
    public String getName4() { return name4; }
    public String getName5() { return name5; }
    public String getName6() { return name6; }
    public String getName7() { return name7; }
    public String getName8() {
        return name8;
    }
    public String getName9() {
        return name9;
    }
    public String getName10() {
        return name10;
    }
    public String getName11() {
        return name11;
    }
    public String getName12() {
        return name12;
    }
    public String getName13() { return name13; }

    public ArrayList<String> getlistaSolicitation() {
        ArrayList<String> listaNames = new ArrayList<String>();
        listaNames.add(name1);
        listaNames.add(name2);
        listaNames.add(name3);
        listaNames.add(name4);
        listaNames.add(name5);
        listaNames.add(name6);
        listaNames.add(name7);
        listaNames.add(name8);
        listaNames.add(name9);
        listaNames.add(name10);
        listaNames.add(name11);
        listaNames.add(name12);
        listaNames.add(name13);

        return listaNames;
    }
}
