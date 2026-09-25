package com.example.nutrilife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.nutrilife.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextNome;
    private TextInputEditText editTextPeso;
    private TextInputEditText editTextAltura;
    private TextInputEditText editTextCintura;
    private TextInputEditText editTextQuadril;
    private RadioGroup radioGroupSexo;
    private Button btnCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextNome = findViewById(R.id.editTextNome);
        editTextPeso = findViewById(R.id.editTextPeso);
        editTextAltura = findViewById(R.id.editTextAltura);
        editTextCintura = findViewById(R.id.editTextCintura);
        editTextQuadril = findViewById(R.id.editTextQuadril);
        radioGroupSexo = findViewById(R.id.radioGroupSexo);
        btnCalcular = findViewById(R.id.button);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNome.getText().toString().trim();
                String pesoStr = editTextPeso.getText().toString().trim();
                String alturaStr = editTextAltura.getText().toString().trim();
                String cinturaStr = editTextCintura.getText().toString().trim();
                String quadrilStr = editTextQuadril.getText().toString().trim();
                int idSexo = radioGroupSexo.getCheckedRadioButtonId();

                if (nome.isEmpty() || pesoStr.isEmpty() || alturaStr.isEmpty()
                        || cinturaStr.isEmpty() || quadrilStr.isEmpty() || idSexo == -1) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("NutriLife")
                            .setMessage("Todos os campos devem ser preenchidos!")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                final double peso, altura, cintura, quadril;
                try {
                    peso = Double.parseDouble(pesoStr.replace(",", "."));
                    altura = Double.parseDouble(alturaStr.replace(",", "."));
                    cintura = Double.parseDouble(cinturaStr.replace(",", "."));
                    quadril = Double.parseDouble(quadrilStr.replace(",", "."));
                } catch (NumberFormatException e) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("NutriLife")
                            .setMessage("Todos os campos devem ser preenchidos!")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                if (peso <= 0 || altura <= 0 || cintura <= 0 || quadril <= 0) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("NutriLife")
                            .setMessage("Todos os campos devem ser preenchidos!")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                RadioButton radioButtonSexo = findViewById(idSexo);
                final String sexo = radioButtonSexo.getText().toString();

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("NutriLife")
                        .setMessage("Todos os dados estão corretos?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
                                intent.putExtra("nome", nome);
                                intent.putExtra("peso", peso);
                                intent.putExtra("altura", altura);
                                intent.putExtra("cintura", cintura);
                                intent.putExtra("quadril", quadril);
                                intent.putExtra("sexo", sexo);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NÃO", null)
                        .show();
            }
        });
    }
}