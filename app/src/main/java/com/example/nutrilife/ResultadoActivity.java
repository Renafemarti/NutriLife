package com.example.nutrilife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ResultadoActivity extends AppCompatActivity {

    private TextView textViewSaudacao;
    private TextView textViewImcLabel;
    private TextView textViewClassificacao;
    private TextView textViewRiscosImc;
    private TextView textViewRcqLabel;
    private TextView textViewRcqRisco;
    private Button buttonVoltar;
    private Button buttonCompartilhar;

    private String nome;
    private double imc;
    private String classificacaoImc;
    private String riscosImc;
    private double rcq;
    private String riscoRcq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewSaudacao = findViewById(R.id.textViewSaudacao);
        textViewImcLabel = findViewById(R.id.textViewImcLabel);
        textViewClassificacao = findViewById(R.id.textViewClassificacao);
        textViewRiscosImc = findViewById(R.id.textViewRiscosImc);
        textViewRcqLabel = findViewById(R.id.textViewRcqLabel);
        textViewRcqRisco = findViewById(R.id.textViewRcqRisco);
        buttonVoltar = findViewById(R.id.buttonVoltar);
        buttonCompartilhar = findViewById(R.id.buttonCompartilhar);

        Intent intentRecebida = getIntent();
        nome = intentRecebida.getStringExtra("nome");
        double peso = intentRecebida.getDoubleExtra("peso", 0);
        double alturaCm = intentRecebida.getDoubleExtra("altura", 0);
        double cintura = intentRecebida.getDoubleExtra("cintura", 0);
        double quadril = intentRecebida.getDoubleExtra("quadril", 0);
        String sexo = intentRecebida.getStringExtra("sexo");

        double alturaM = alturaCm / 100.0;

        imc = peso / (alturaM * alturaM);
        classificarImc(imc);

        rcq = cintura / quadril;
        classificarRcq(rcq, sexo);

        exibirResultados();

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharResultado();
            }
        });
    }

    private void classificarImc(double valor) {
        if (valor < 17.0) {
            classificacaoImc = "Muito abaixo do peso";
            riscosImc = "Atenção! Procure orientação nutricional.";
        } else if (valor < 18.5) {
            classificacaoImc = "Abaixo do peso";
            riscosImc = "Fique atento à sua alimentação.";
        } else if (valor < 25.0) {
            classificacaoImc = "Peso normal";
            riscosImc = "Você está dentro do peso adequado.";
        } else if (valor < 30.0) {
            classificacaoImc = "Sobrepeso";
            riscosImc = "Fique de olho, pequenos ajustes podem ajudar pra abaixar o IMC.";
        } else if (valor < 35.0) {
            classificacaoImc = "Obesidade Grau I";
            riscosImc = "Considere buscar acompanhamento profissional.";
        } else if (valor < 40.0) {
            classificacaoImc = "Obesidade Grau II";
            riscosImc = "Procure orientação médica o quanto antes.";
        } else {
            classificacaoImc = "Obesidade Grau III";
            riscosImc = "Busque acompanhamento médico com urgência.";
        }
    }

    private void classificarRcq(double valor, String sexo) {
        boolean masculino = sexo != null && sexo.equalsIgnoreCase("Masculino");
        if (masculino) {
            riscoRcq = (valor < 0.90) ? "Normal" : "Risco aumentado";
        } else {
            riscoRcq = (valor < 0.85) ? "Normal" : "Risco aumentado";
        }
    }

    private void exibirResultados() {
        textViewSaudacao.setText(String.format("Olá, %s!", nome));
        textViewImcLabel.setText(String.format(Locale.getDefault(), "Seu IMC é: %.2f kg/m²", imc));
        textViewClassificacao.setText(String.format("Classificação: %s", classificacaoImc));
        textViewRiscosImc.setText(riscosImc);
        textViewRcqLabel.setText(String.format(Locale.getDefault(), "Sua RCQ é: %.2f", rcq));
        textViewRcqRisco.setText(String.format("Risco relacionado à RCQ: %s", riscoRcq));
    }

    private void compartilharResultado() {
        String mensagem = String.format(Locale.getDefault(),
                "Resultado NutriLife\n\n" +
                        "Nome: %s\n" +
                        "IMC: %.2f kg/m² (%s)\n" +
                        "%s\n" +
                        "RCQ: %.2f (%s)",
                nome, imc, classificacaoImc, riscosImc, rcq, riscoRcq);

        Intent intentCompartilhar = new Intent(Intent.ACTION_SEND);
        intentCompartilhar.setType("text/plain");
        intentCompartilhar.putExtra(Intent.EXTRA_TEXT, mensagem);
        startActivity(Intent.createChooser(intentCompartilhar, "Compartilhar resultado via"));
    }
}