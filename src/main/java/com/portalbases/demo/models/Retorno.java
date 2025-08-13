package com.portalbases.demo.models;
import java.sql.Timestamp;
import java.util.List;

public class Retorno {
    private List<String> base;
    private List<Timestamp> ultimaOperacao;

    public Retorno(List<String> base, List<Timestamp> ultimaOperacao) {
        this.base = base;
        this.ultimaOperacao = ultimaOperacao;
    }

    public List<String> getBase() {
        return base;
    }

    public List<Timestamp> getUltimaOperacao() {
        return ultimaOperacao;
    }
}