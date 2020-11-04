package com.anyvision.facekeyexample.utils;

public class Enum {

    public enum AgReg {
        AGENCIA,
        REGIONAL
    }

    public enum request {
        state,
        descriptions,
        aprovaReprovaExtesao,
        chamadoDescriptionsButtons,
    }

    public enum SharedPrivate {
        TIPO_AGENCIA_REGIONAL,
        URL_ANYVISION,
        URL_SERVIDOR_LOCAL,
        GRAFICO_CHAMADO,
        CHAMADO_GESTAO_VALOR_TOTAL,
        CHAMADO_GESTAO_CONTROLE_SALA_SIZE
    }

    public enum LogarSemSesame {
        LOGAR,
        MUDARSENHA,
        GRAFICO_GESTAO
    }

    public enum StatusSolicitacao{
        AGUARDANDO,
        APROVADO,
        REPROVADO;
    }

    public enum Chamado{
        CFTV,
        ALARME,
        INCENDIO,
        HVAC,
        ARCONDICIONADO,
        SISTEMA_INCÊNDIO
    }
}
