

import static android.os.Build.VERSION_CODES_FULL.R;

import miPrimeraApp.R;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TabHost tbh;
    TextView tempVal;
    Spinner spn;
    Button btn;

    String[] Area = {"Pie Cuadrado", "Vara Cuadrada", "Yarda Cuadrada", "Metro Cuadrado", "Tareas", "Manzana", "Hectárea"};
    Double valores[] = new Double[]{0.092903, 0.69874, 0.836127, 1.0, 437.5, 7000.0, 10000.0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();

        TabHost.TabSpec tsArea = tbh.newTabSpec("Area");
        tsArea.setContent(R.id.tabArea);
        tsArea.setIndicator("ÁREA");
        tbh.addTab(tsArea);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Area);

        spn = findViewById(R.id.spnAreaDe);
        spn.setAdapter(adapter);

        Spinner spnA = findViewById(R.id.spnAreaA);
        spnA.setAdapter(adapter);

        btn = findViewById(R.id.btnAreaConvertir);
        btn.setOnClickListener(v -> convertirArea());
    }

    private void convertirArea() {
        spn = findViewById(R.id.spnAreaDe);
        int de = spn.getSelectedItemPosition();

        Spinner spnDestino = findViewById(R.id.spnAreaA);
        int a = spnDestino.getSelectedItemPosition();

        tempVal = findViewById(R.id.txtAreaCantidad);
        double cantidad = Double.parseDouble(tempVal.getText().toString());

        double respuesta = conversor(de, a, cantidad);

        tempVal = findViewById(R.id.lblAreaRespuesta);
        tempVal.setText("Respuesta: " + respuesta);
    }

    double conversor(int de, int a, double cantidad) {
        return (valores[de] / valores[a]) * cantidad;
    }
}
