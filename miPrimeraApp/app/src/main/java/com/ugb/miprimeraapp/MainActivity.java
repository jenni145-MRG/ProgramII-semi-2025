package com.ugb.miprimeraapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TabHost;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tempVal;
    Spinner spn;
    Button btn;

    // Matriz corregida para que coincida con el orden de las etiquetas
    Double valores[][] = {
            {1.0, 0.92, 7.75, 24.63, 36.62, 513.15}, // Monedas (Base: Dólar)
            {1.0, 2.20462, 1000.0, 35.274}, // Masa (Base: Kilogramo)
            {1.0, 1000.0, 100.0, 39.3701, 3.28084, 1.1963, 1.09361}, // Longitud (Base: Metro)
            {1.0, 0.264172, 1000.0, 0.001}, // Volumen (Base: Litro)
            {1.0, 0.00097656, 0.00000095, 0.0000000009, 0.0000000000009}, // Almacenamiento (Base: KB)
            {1.0, 1.0/60.0, 1.0/3600.0, 1.0/86400.0, 1.0/604800.0, 1.0/2629743.0, 1.0/31556926.0} // Tiempo (Base: Segundo)
    };

    String[][] etiquetas = {
            {"Dolar", "Euro", "Quetzal", "Lempira", "Cordoba", "Colon CR"},
            {"Kilogramo","Libra","Gramo","Onza"},
            {"Mts", "Ml", "Cm", "Pulgada", "Pies", "Vara", "Yarda"},
            {"Litro","Galon","Mililitro","Metros Cubicos"},
            {"KB","MB","GB","TB","PB"},
            {"Segundo","Minuto","Hora","Dia","Semana","Mes","Año"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnConvertir);
        btn.setOnClickListener(v->convertir());

        spn = findViewById(R.id.spnTipo);
        cambiarEtiqueta(0);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cambiarEtiqueta(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void cambiarEtiqueta(int posicion){
        ArrayAdapter<String> aaEtiquetas = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                etiquetas[posicion]
        );
        aaEtiquetas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spnDe = findViewById(R.id.spnDe);
        spnDe.setAdapter(aaEtiquetas);

        Spinner spnA = findViewById(R.id.spnA);
        spnA.setAdapter(aaEtiquetas);
    }

    private void convertir(){
        try {
            spn = findViewById(R.id.spnTipo);
            int tipo = spn.getSelectedItemPosition();

            Spinner spnDe = findViewById(R.id.spnDe);
            int de = spnDe.getSelectedItemPosition();

            Spinner spnA = findViewById(R.id.spnA);
            int a = spnA.getSelectedItemPosition();

            tempVal = findViewById(R.id.txtCantidad);
            double cantidad = Double.parseDouble(tempVal.getText().toString());

            double respuesta = conversor(tipo, de, a, cantidad);

            TextView lblRespuesta = findViewById(R.id.lblRespuesta);
            lblRespuesta.setText("Respuesta: " + String.format("%.2f", respuesta));
            Toast.makeText(this,"Calculado correctamente",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: Ingrese un valor válido", Toast.LENGTH_SHORT).show();
        }
    }

    double conversor(int tipo, int de, int a, double cantidad){
        return (valores[tipo][a] / valores[tipo][de]) * cantidad;
    }
}