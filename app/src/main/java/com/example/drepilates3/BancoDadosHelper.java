package com.example.drepilates3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BancoDadosHelper extends SQLiteOpenHelper {

    private static final String BANCO = "DrePilates.db";
    private static final int VERSAO = 1;

    public BancoDadosHelper(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE alunos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "telefone TEXT, " +
                "email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean inserirAluno(String nome, String telefone, String email) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("telefone", telefone);
        valores.put("email", email);

        return db.insert("alunos", null, valores) != -1;
    }

    public ArrayList<String> listarAlunos() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nome FROM alunos ORDER BY nome",
                null
        );

        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0));
        }

        cursor.close();
        return lista;
    }
}