package com.example.drepilates3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drepilates3.model.Aluno;
import com.example.drepilates3.model.Agendamento;

import java.util.ArrayList;

public class BancoDados extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DREPilates.db";
    private static final int DATABASE_VERSION = 1;

    public BancoDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE alunos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "telefone TEXT," +
                "email TEXT," +
                "ativo INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE agendamentos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "aluno_id INTEGER NOT NULL," +
                "nome_aluno TEXT NOT NULL," +
                "data TEXT NOT NULL," +
                "horario TEXT NOT NULL," +
                "observacao TEXT," +
                "status TEXT DEFAULT 'Agendado')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizações futuras do banco serão feitas aqui.
    }

    // =========================
    // ALUNOS
    // =========================

    public long inserirAluno(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", aluno.getNome());
        valores.put("telefone", aluno.getTelefone());
        valores.put("email", aluno.getEmail());
        valores.put("ativo", 1);

        return db.insert("alunos", null, valores);
    }

    public ArrayList<Aluno> listarAlunos() {
        ArrayList<Aluno> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM alunos WHERE ativo = 1 ORDER BY nome ASC",
                null
        );

        try {
            while (cursor.moveToNext()) {
                lista.add(new Aluno(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ativo")) == 1
                ));
            }
        } finally {
            cursor.close();
        }

        return lista;
    }

    public ArrayList<Aluno> buscarAlunos(String termo) {
        ArrayList<Aluno> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM alunos WHERE ativo = 1 AND nome LIKE ? ORDER BY nome ASC",
                new String[]{"%" + termo + "%"}
        );

        try {
            while (cursor.moveToNext()) {
                lista.add(new Aluno(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ativo")) == 1
                ));
            }
        } finally {
            cursor.close();
        }

        return lista;
    }

    public int atualizarAluno(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", aluno.getNome());
        valores.put("telefone", aluno.getTelefone());
        valores.put("email", aluno.getEmail());

        return db.update(
                "alunos",
                valores,
                "id = ?",
                new String[]{String.valueOf(aluno.getId())}
        );
    }

    public int desativarAluno(int id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("ativo", 0);

        return db.update(
                "alunos",
                valores,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // =========================
    // AGENDAMENTOS
    // =========================

    public long inserirAgendamento(Agendamento agendamento) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("aluno_id", agendamento.getAlunoId());
        valores.put("nome_aluno", agendamento.getNomeAluno());
        valores.put("data", agendamento.getData());
        valores.put("horario", agendamento.getHorario());
        valores.put("observacao", agendamento.getObservacao());
        valores.put("status", "Agendado");

        return db.insert("agendamentos", null, valores);
    }

    public ArrayList<Agendamento> listarAgendamentos() {
        ArrayList<Agendamento> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM agendamentos WHERE status = 'Agendado' " +
                        "ORDER BY data ASC, horario ASC",
                null
        );

        try {
            while (cursor.moveToNext()) {
                lista.add(new Agendamento(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("aluno_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome_aluno")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data")),
                        cursor.getString(cursor.getColumnIndexOrThrow("horario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("observacao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                ));
            }
        } finally {
            cursor.close();
        }

        return lista;
    }

    public int cancelarAgendamento(int id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("status", "Cancelado");

        return db.update(
                "agendamentos",
                valores,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public boolean horarioOcupado(String data, String horario) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM agendamentos " +
                        "WHERE data = ? AND horario = ? AND status = 'Agendado'",
                new String[]{data, horario}
        );

        boolean ocupado = cursor.moveToFirst();
        cursor.close();

        return ocupado;
    }

    public int contarAlunosAtivos() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM alunos WHERE ativo = 1",
                null
        );

        int quantidade = 0;

        if (cursor.moveToFirst()) {
            quantidade = cursor.getInt(0);
        }

        cursor.close();
        return quantidade;
    }

    public int contarAgendamentos() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM agendamentos WHERE status = 'Agendado'",
                null
        );

        int quantidade = 0;

        if (cursor.moveToFirst()) {
            quantidade = cursor.getInt(0);
        }

        cursor.close();
        return quantidade;
    }
}