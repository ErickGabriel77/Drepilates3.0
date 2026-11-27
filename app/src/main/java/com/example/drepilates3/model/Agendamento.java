package com.example.drepilates3.model;

public class Agendamento {

    private int id;
    private int alunoId;
    private String nomeAluno;
    private String data;
    private String horario;
    private String observacao;
    private String status;

    public Agendamento(int id, int alunoId, String nomeAluno,
                       String data, String horario,
                       String observacao, String status) {
        this.id = id;
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.data = data;
        this.horario = horario;
        this.observacao = observacao;
        this.status = status;
    }

    public Agendamento(int alunoId, String nomeAluno,
                       String data, String horario, String observacao) {
        this(0, alunoId, nomeAluno, data, horario, observacao, "Agendado");
    }

    public int getId() {
        return id;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}