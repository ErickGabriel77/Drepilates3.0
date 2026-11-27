
package com.example.drepilates3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drepilates3.database.BancoDados;
import com.example.drepilates3.model.Aluno;

public class CadastroAlunoActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private Button btnSalvar;

    private BancoDados bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);

        bancoDados = new BancoDados(this);

        edtNome = findViewById(R.id.edtNomeAluno);
        edtTelefone = findViewById(R.id.edtTelefoneAluno);
        edtEmail = findViewById(R.id.edtEmailAluno);
        btnSalvar = findViewById(R.id.btnSalvarAluno);

        btnSalvar.setOnClickListener(v -> salvarAluno());
    }

    private void salvarAluno() {

        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (nome.isEmpty()) {
            edtNome.setError("Digite o nome do aluno");
            edtNome.requestFocus();
            return;
        }

        Aluno aluno = new Aluno(nome, telefone, email);

        try {
            long resultado = bancoDados.inserirAluno(aluno);

            if (resultado != -1) {

                Toast.makeText(this,
                        "Aluno cadastrado com sucesso!",
                        Toast.LENGTH_SHORT).show();

                finish();

            } else {

                Toast.makeText(this,
                        "Não foi possível salvar o aluno.",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(this,
                    "Erro ao salvar: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bancoDados != null) {
            bancoDados.close();
        }
    }
}
