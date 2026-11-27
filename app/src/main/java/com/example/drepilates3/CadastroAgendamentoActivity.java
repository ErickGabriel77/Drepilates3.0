
package com.example.drepilates3;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drepilates3.database.BancoDados;
import com.example.drepilates3.model.Aluno;
import com.example.drepilates3.model.Agendamento;

import java.util.ArrayList;

public class CadastroAgendamentoActivity extends AppCompatActivity {

    Spinner spnAluno;
    EditText edtData, edtHorario, edtObservacao;
    Button btnSalvarAgendamento;

    BancoDados bancoDados;
    ArrayList<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_agendamento);

        bancoDados = new BancoDados(this);

        spnAluno = findViewById(R.id.spnAlunoAgendamento);
        edtData = findViewById(R.id.edtDataAgendamento);
        edtHorario = findViewById(R.id.edtHorarioAgendamento);
        edtObservacao = findViewById(R.id.edtObservacaoAgendamento);
        btnSalvarAgendamento = findViewById(R.id.btnSalvarAgendamento);

        carregarAlunos();

        btnSalvarAgendamento.setOnClickListener(v -> {

            if (alunos.isEmpty()) {
                Toast.makeText(this,
                        "Cadastre um aluno primeiro!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String data = edtData.getText().toString().trim();
            String horario = edtHorario.getText().toString().trim();
            String observacao = edtObservacao.getText().toString().trim();

            if (data.isEmpty() || horario.isEmpty()) {
                Toast.makeText(this,
                        "Preencha a data e o horário",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Aluno alunoSelecionado =
                    alunos.get(spnAluno.getSelectedItemPosition());

            if (bancoDados.horarioOcupado(data, horario)) {
                Toast.makeText(this,
                        "Este horário já está ocupado!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Agendamento agendamento = new Agendamento(
                    alunoSelecionado.getId(),
                    alunoSelecionado.getNome(),
                    data,
                    horario,
                    observacao
            );

            long resultado =
                    bancoDados.inserirAgendamento(agendamento);

            if (resultado != -1) {
                Toast.makeText(this,
                        "Agendamento cadastrado com sucesso!",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this,
                        "Erro ao cadastrar agendamento!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarAlunos() {

        alunos = bancoDados.listarAlunos();

        ArrayList<String> nomes = new ArrayList<>();

        for (Aluno aluno : alunos) {
            nomes.add(aluno.getNome());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nomes
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spnAluno.setAdapter(adapter);
    }
}
