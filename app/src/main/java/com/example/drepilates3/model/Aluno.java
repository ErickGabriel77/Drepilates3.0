package com.example.drepilates3.model;

public class Aluno {

    private int id;
    private String nome;
    private String telefone;
    private String email;
    private boolean ativo;

    public Aluno(int id, String nome, String telefone, String email, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.ativo = ativo;
    }

    public Aluno(String nome, String telefone, String email) {
        this(0, nome, telefone, email, true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}