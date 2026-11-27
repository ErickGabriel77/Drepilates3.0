package com.example.drepilates3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAlunos, btnAgenda, btnAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlunos = findViewById(R.id.btnAlunos);
        btnAgenda = findViewById(R.id.btnAgenda);
        btnAulas = findViewById(R.id.btnAulas);

        btnAlunos.setOnClickListener(v ->
                startActivity(new Intent(this, AlunosActivity.class)));

        btnAgenda.setOnClickListener(v ->
                startActivity(new Intent(this, AgendaActivity.class)));

        btnAulas.setOnClickListener(v ->
                startActivity(new Intent(this, AulasActivity.class)));
    }
}