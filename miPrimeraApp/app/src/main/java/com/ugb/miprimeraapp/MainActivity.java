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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


        TextView tempVal;
        Spinner spn;
        Button btn;
        Double valores[][] = {
                {1.0, 0.85, 7.67, 26.42, 36.80, 495.77}, //monedas
                {1.0,2.20462,1000.0,35.274},//Masa
                {1.0, 1000.0, 100.0, 39.3701, 3.280841666667, 1.1963081929167, 1.09361}, //longitud
                {1.0,0.264172,1000.0,0.001}, //volumen
                {1.0,0.00097656,0.00000095,0.0000000009,0.0000000000009},//Almacenamiento
                {1.0,60.0,3600.0,86400.0,604800.0,2629743.0,31556926.0}//Tiempo
        };
        String[][] etiquetas = {
                {"Dolar", "Euro", "Quetzal", "Lempira", "Cordoba", "Colon CR"}, //monedas
                {"Kilogramo","Libra","Gramo","Onza",},//Masa
                {"Mts", "Ml", "Cm", "Pulgada", "Pies", "Vara", "Yarda"}, //Longitud
                {"Litro","Galon","Mililitro","Metros Cubicos"},  //volumen
                {"KB","MB","GB","TB","PB"}, //Almacenamiento
                {"Segundo","Minuto","Hora","Dia","Semana","Mes","Año"}//Tiempo

        };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            btn = findViewById(R.id.btnConvertir);
            btn.setOnClickListener(v->convertir());

            cambiarEtiqueta(0);//valores predeterminaods

            spn = findViewById(R.id.spnTipo);
            spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    cambiarEtiqueta(i);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        private void cambiarEtiqueta(int posicion){
            ArrayAdapter<String> aaEtiquetas = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    etiquetas[posicion]
            );
            aaEtiquetas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn = findViewById(R.id.spnDe);
            spn.setAdapter(aaEtiquetas);

            spn = findViewById(R.id.spnA);
            spn.setAdapter(aaEtiquetas);
        }
        private void convertir(){
            spn = findViewById(R.id.spnTipo);
            int tipo = spn.getSelectedItemPosition();
            spn = findViewById(R.id.spnDe);
            int de = spn.getSelectedItemPosition();

            spn = findViewById(R.id.spnA);
            int a = spn.getSelectedItemPosition();

            tempVal = findViewById(R.id.txtCantidad);
            double cantidad = Double.parseDouble(tempVal.getText().toString());
            double respuesta = conversor(tipo, de, a, cantidad);

            tempVal = findViewById(R.id.lblRespuesta);
            tempVal.setText("Respuesta: "+ respuesta);
            Toast.makeText(this,"Calculado correctamente",Toast.LENGTH_SHORT).show();
        }
        double conversor(int tipo, int de, int a, double cantidad){
            return valores[tipo][de]/valores[tipo][a] * cantidad;


        }
    }