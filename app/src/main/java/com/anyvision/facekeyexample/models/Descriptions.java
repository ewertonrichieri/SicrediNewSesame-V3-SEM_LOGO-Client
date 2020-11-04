package com.anyvision.facekeyexample.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "VariableRow", strict = false)
public class Descriptions {

    @Element(name="Description", required = false)
    private String Description;

    public String GetDescription(){
        return Description;
    }

}
