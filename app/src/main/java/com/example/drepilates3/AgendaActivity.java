
package com.example.drepilates3;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drepilates3.database.BancoDados;
import com.example.drepilates3.model.Agendamento;

import java.util.ArrayList;

public class AgendaActivity extends AppCompatActivity {

    Button btnNovoAgendamento;
    TextView txtTotalAgendamentos;
    ListView listaAgendamentos;

    ArrayList<String> agendamentos;
    ArrayList<Agendamento> agendamentosExibidos;

    ArrayAdapter<String> adapter;

    BancoDados bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        btnNovoAgendamento = findViewById(R.id.btnNovoAgendamento);
        txtTotalAgendamentos = findViewById(R.id.txtTotalAgendamentos);
        listaAgendamentos = findViewById(R.id.listaAgendamentos);

        bancoDados = new BancoDados(this);

        agendamentos = new ArrayList<>();
        agendamentosExibidos = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                agendamentos
        );

        listaAgendamentos.setAdapter(adapter);

        btnNovoAgendamento.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AgendaActivity.this,
                    CadastroAgendamentoActivity.class
            );
            startActivity(intent);
        });

        // Cancelar agendamento segurando o dedo
        listaAgendamentos.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    Agendamento agendamento =
                            agendamentosExibidos.get(position);

                    new AlertDialog.Builder(this)
                            .setTitle("Cancelar agendamento")
                            .setMessage(
                                    "Deseja realmente cancelar o agendamento de "
                                            + agendamento.getNomeAluno()
                                            + "?\n\nData: "
                                            + agendamento.getData()
                                            + "\nHorário: "
                                            + agendamento.getHorario()
                            )
                            .setNegativeButton("Voltar", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .setPositiveButton("Cancelar agendamento",
                                    (dialog, which) -> {

                                        int resultado =
                                                bancoDados.cancelarAgendamento(
                                                        agendamento.getId()
                                                );

                                        if (resultado > 0) {

                                            Toast.makeText(
                                                    this,
                                                    "Agendamento cancelado com sucesso!",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Não foi possível cancelar.",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }

                                        carregarAgendamentos();
                                    })
                            .show();

                    return true;
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAgendamentos();
    }

    private void carregarAgendamentos() {

        ArrayList<Agendamento> lista =
                bancoDados.listarAgendamentos();

        agendamentos.clear();
        agendamentosExibidos.clear();

        for (Agendamento agendamento : lista) {

            String texto = "Aluno: " + agendamento.getNomeAluno()
                    + "\nData: " + agendamento.getData()
                    + "\nHorário: " + agendamento.getHorario()
                    + "\nStatus: " + agendamento.getStatus();

            agendamentos.add(texto);
            agendamentosExibidos.add(agendamento);
        }

        adapter.notifyDataSetChanged();

        txtTotalAgendamentos.setText(
                "Agendamentos cadastrados: " + agendamentos.size()
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
