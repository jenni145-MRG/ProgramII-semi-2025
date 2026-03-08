package com.ugb.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText etCodigo, etDescripcion, etMarca, etPrecio;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Configuración de los bordes de la pantalla (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




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

        etCodigo = findViewById(R.id.etCodigo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etMarca = findViewById(R.id.etMarca);
        etPrecio = findViewById(R.id.etPrecio);
    }


    public void registrar(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "tienda", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("Codigo", etCodigo.getText().toString());
        registro.put("Descripcion", etDescripcion.getText().toString());
        registro.put("Marca", etMarca.getText().toString());
        registro.put("Precio", etPrecio.getText().toString());

        bd.insert("Informacion", null, registro);
        bd.close();

        etCodigo.setText("");
        etDescripcion.setText("");
        etMarca.setText("");
        etPrecio.setText("");

        Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
    }
}








