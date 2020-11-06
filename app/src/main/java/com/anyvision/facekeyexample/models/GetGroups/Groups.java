package com.anyvision.facekeyexample.models.GetGroups;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class Groups {

    @ElementList(entry = "GroupRow", inline = true, required = false)
    private List<GroupRow> group;

    public ArrayList<String> getGroup() {
        ArrayList<String> lista = new ArrayList<String>();

        for(GroupRow li : group){
            lista.add(li.getName());
        }
        return lista;
    }
}