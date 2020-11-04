package com.anyvision.facekeyexample.models;

import com.google.gson.internal.bind.JsonTreeReader;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class ChamadoGrafico {

    @ElementList(entry = "VariableState", inline = true, required = false)
    private List<VariableState> listaVariableStates;

    public ArrayList<String> GetListaGestaoControleSala(){
        ArrayList<String> listaGestao = new ArrayList<String>();

        for(VariableState li : listaVariableStates){
            listaGestao.add(li.GetName() + ";" + li.GetValor());
        }

        return listaGestao;
    }

    //Pega a soma dos valores para ser apresentado no grafico
    public int GetPorcentagemTotalGestaoControleSala() {
        int soma = 0;

        for(VariableState valor : listaVariableStates){
            soma += Integer.parseInt(valor.GetValor());
        }

        return soma;
    }
}
