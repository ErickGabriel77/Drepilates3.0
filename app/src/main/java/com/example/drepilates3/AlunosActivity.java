
package com.example.drepilates3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drepilates3.database.BancoDados;
import com.example.drepilates3.model.Aluno;

import java.util.ArrayList;

public class AlunosActivity extends AppCompatActivity {

    EditText edtBuscarAluno;
    Button btnNovoAluno;
    TextView txtTotalAlunos;
    ListView listaAlunos;

    ArrayList<String> nomesAlunos;
    ArrayList<Aluno> alunosExibidos;

    ArrayAdapter<String> adapter;

    BancoDados bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alunos);

        edtBuscarAluno = findViewById(R.id.edtBuscarAluno);
        btnNovoAluno = findViewById(R.id.btnNovoAluno);
        txtTotalAlunos = findViewById(R.id.txtTotalAlunos);
        listaAlunos = findViewById(R.id.listaAlunos);

        bancoDados = new BancoDados(this);

        nomesAlunos = new ArrayList<>();
        alunosExibidos = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nomesAlunos
        );

        listaAlunos.setAdapter(adapter);

        btnNovoAluno.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    CadastroAlunoActivity.class
            );
            startActivity(intent);
        });

        edtBuscarAluno.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

                atualizarLista(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Desativar aluno segurando o dedo no nome
        listaAlunos.setOnItemLongClickListener((parent, view, position, id) -> {

            Aluno aluno = alunosExibidos.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Desativar aluno")
                    .setMessage(
                            "Deseja realmente desativar o aluno "
                                    + aluno.getNome() + "?"
                    )
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Desativar", (dialog, which) -> {

                        int resultado = bancoDados.desativarAluno(
                                aluno.getId()
                        );

                        if (resultado > 0) {

                            Toast.makeText(
                                    this,
                                    "Aluno desativado com sucesso!",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Não foi possível desativar o aluno.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        atualizarLista(
                                edtBuscarAluno.getText().toString()
                        );
                    })
                    .show();

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bancoDados != null) {
            atualizarLista(edtBuscarAluno.getText().toString());
        }
    }

    private void atualizarLista(String termo) {

        ArrayList<Aluno> alunos;

        if (termo == null || termo.trim().isEmpty()) {
            alunos = bancoDados.listarAlunos();
        } else {
            alunos = bancoDados.buscarAlunos(termo.trim());
        }

        nomesAlunos.clear();
        alunosExibidos.clear();

        for (Aluno aluno : alunos) {
            alunosExibidos.add(aluno);
            nomesAlunos.add(aluno.getNome());
        }

        adapter.notifyDataSetChanged();

        txtTotalAlunos.setText(
                "Alunos cadastrados: " +
                        bancoDados.contarAlunosAtivos()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bancoDados != null) {
            bancoDados.close();
        }
    }
}
