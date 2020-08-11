package com.anyvision.facekeyexample.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import java.util.ArrayList;

@Root(strict = false)
public class VariableRowChamado {

    @Element(name = "Description", required = false)
    @Path("VariableRow[1]")
    private String descriptionChamado1;

    @Element(name = "Description", required = false)
    @Path("VariableRow[2]")
    private String descriptionChamado2;

    @Element(name = "Description", required = false)
    @Path("VariableRow[3]")
    private String descriptionChamado3;

    @Element(name = "Description", required = false)
    @Path("VariableRow[4]")
    private String descriptionChamado4;

    @Element(name = "Description", required = false)
    @Path("VariableRow[5]")
    private String descriptionChamado5;

    @Element(name = "Description", required = false)
    @Path("VariableRow[6]")
    private String descriptionChamado6;

    public ArrayList<String> getListChamado() {
        ArrayList<String> listChamado = new ArrayList<String>();
        if (descriptionChamado1 != null) listChamado.add(descriptionChamado1);
        if (descriptionChamado2 != null) listChamado.add(descriptionChamado2);
        if (descriptionChamado3 != null) listChamado.add(descriptionChamado3);
        if (descriptionChamado4 != null) listChamado.add(descriptionChamado4);
        if (descriptionChamado5 != null) listChamado.add(descriptionChamado5);
        if (descriptionChamado6 != null) listChamado.add(descriptionChamado6);

        return listChamado;
    }
}