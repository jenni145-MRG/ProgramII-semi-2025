package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    Spinner spnAreaDe, spnAreaA;
    Button btn, btnAgua;
    EditText txtMetros, txAreaCantidad;
    TextView lblAguaRespuesta, lblAreaRespuesta;

    Double valorArea[] = new Double[]{0.092903, 0.698756, 0.836127, 1.0, 436.722, 6987.557, 10000.0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();


        tbh.addTab(tbh.newTabSpec("Area")
                .setContent(R.id.Area)
                .setIndicator("Área", null));

        tbh.addTab(tbh.newTabSpec("Agua")
                .setContent(R.id.Agua)
                .setIndicator("Agua", null));


        spnAreaDe      = findViewById(R.id.AreaDe);
        spnAreaA       = findViewById(R.id.spnAreaA);
        txAreaCantidad = findViewById(R.id.txAreaCantidad);
        lblAreaRespuesta = findViewById(R.id.lblAreaRespuesta);

        btn = findViewById(R.id.btnAreaConvertir);
        btn.setOnClickListener(v -> convertirArea());


        txtMetros        = findViewById(R.id.txtMetros);
        lblAguaRespuesta = findViewById(R.id.lblAguaRespuesta);

        btnAgua = findViewById(R.id.btnAguaCalcular);
        btnAgua.setOnClickListener(v -> calcularAgua());
    }


    private void convertirArea() {
        String input = txAreaCantidad.getText().toString();

        if (input.isEmpty()) {
            lblAreaRespuesta.setText("Ingrese una cantidad");
            return;
        }

        int de = spnAreaDe.getSelectedItemPosition();
        int a  = spnAreaA.getSelectedItemPosition();
        double cantidad  = Double.parseDouble(input);
        double respuesta = conversor(de, a, cantidad, valorArea);

        lblAreaRespuesta.setText(String.format("Respuesta: %.6f", respuesta));
    }


    private void calcularAgua() {
        String input = txtMetros.getText().toString();

        if (input.isEmpty()) {
            lblAguaRespuesta.setText("Ingrese los metros consumidos");
            return;
        }

        double metros = Double.parseDouble(input);
        double total;

        if (metros <= 18) {

            total = 6.0;

        } else if (metros <= 28) {
            total = 6.0 + (metros - 18) * 0.45;

        } else {

            total = 6.0 + (10 * 0.45) + (metros - 28) * 0.65;
        }

        lblAguaRespuesta.setText(String.format("Valor a pagar: $%.2f", total));
    }


    double conversor(int de, int a, double cantidad, Double[] valoresArray) {
        return cantidad * valoresArray[de] / valoresArray[a];
    }
}