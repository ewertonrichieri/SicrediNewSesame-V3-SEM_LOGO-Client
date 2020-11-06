package com.anyvision.facekeyexample.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class Facilities {

    @ElementList(entry = "VariableRow", inline = true, required = false)
    private List<Descriptions> facilitiesDescriptions;

    public ArrayList<String> GetDescricaoBtnFacilities() {
        ArrayList<String> listaDescricao = new ArrayList<String>();

        for (Descriptions li : facilitiesDescriptions) {
            listaDescricao.add(li.GetDescription());
        }
        return listaDescricao;
    }
}
