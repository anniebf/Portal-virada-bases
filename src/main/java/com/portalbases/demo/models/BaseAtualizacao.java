package com.portalbases.demo.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BaseAtualizacao {
    private String nomeBase;
    private LocalDate ultimaAtualizacao;
    private LocalDate proximaAtualizacao;

    // Construtor
    public BaseAtualizacao() {
    }

    public BaseAtualizacao(String nomeBase, LocalDate ultimaAtualizacao, LocalDate proximaAtualizacao) {
        this.nomeBase = nomeBase;
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.proximaAtualizacao = proximaAtualizacao;
    }

    // Getters e Setters
    public String getNomeBase() {
        return nomeBase;
    }

    public void setNomeBase(String nomeBase) {
        this.nomeBase = nomeBase;
    }

    public LocalDate getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDate ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public LocalDate getProximaAtualizacao() {
        return proximaAtualizacao;
    }

    public void setProximaAtualizacao(LocalDate proximaAtualizacao) {
        this.proximaAtualizacao = proximaAtualizacao;
    }

    public String getUltimaAtualizacaoFormatada() {
        if (ultimaAtualizacao == null) return "N/A";
        return ultimaAtualizacao.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // <-- formato igual ao CSV
    }

    public String getProximaAtualizacaoFormatada() {
        if (proximaAtualizacao == null) return "N/A";
        return proximaAtualizacao.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // <-- formato igual ao CSV
    }

}

