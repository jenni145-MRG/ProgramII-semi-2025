package com.ugb.myapplication;

import android.os.Bundle;
import android.widget.TabHost;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configuración de los bordes de la pantalla (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- EL CÓDIGO DEL TABHOST DEBE IR AQUÍ AFUERA ---

        // 1. Conectar el TabHost del XML a Java
        TabHost abh = findViewById(R.id.tbhTienda);
        abh.setup();

        // 2. Configurar la pestaña de "Información"
        TabHost.TabSpec spec = abh.newTabSpec("info");
        spec.setContent(R.id.pestañaInfo); // Asegúrate de que en el XML sea idéntico (con o sin ñ)
        spec.setIndicator("INFORMACIÓN");
        abh.addTab(spec);

        // 3. Crear pestañas para "Producto" y "Compra"
        // Usamos el ID del FrameLayout o un TextView temporal para que no de error
        abh.addTab(abh.newTabSpec("producto").setContent(R.id.textView).setIndicator("PRODUCTO"));
        abh.addTab(abh.newTabSpec("compra").setContent(R.id.textView).setIndicator("COMPRA"));
    }
}
