package com.anyvision.facekeyexample.models.FacilitiesModel;

import com.anyvision.facekeyexample.utils.Enum;

public class FacilitiesModel {

        static public String Data;
        static public String QtdHoras;
        static public String QtdProfissionais;
        static public String Opcao;

        public FacilitiesModel(){}

         public FacilitiesModel(String Data, String QtdHoras, String QtdProfissionais, String Opcao){
            this.Data = Data;
            this.QtdHoras = QtdHoras;
            this.QtdProfissionais = QtdProfissionais;
            this.Opcao = Opcao;
        }

        public static String getData(){
            return Data;
        }

        public static String getQtdHoras(){
            return QtdHoras;
        }

        public static String getQtdProfissionais(){
            return QtdProfissionais;
        }

        public static String getOpcao(){
            return Opcao;
        }
    }

