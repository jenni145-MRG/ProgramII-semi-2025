package com.ugb.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TabHost host;
    EditText txtCodigo, txtDescripcion, txtMarca, txtPresentacion, txtPrecio;
    Button btnAgregar, btnModificar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarTabHost();
        inicializarControles();

        btnAgregar.setOnClickListener(v -> registrarProducto());
        btnEliminar.setOnClickListener(v -> eliminarProducto());
    }

    private void inicializarTabHost() {
        host = findViewById(R.id.miTabHost);
        host.setup();

        // Pestaña 1: Registro
        TabHost.TabSpec spec1 = host.newTabSpec("tab1");
        spec1.setIndicator("REGISTRO");
        spec1.setContent(R.id.pestaña_registro);
        host.addTab(spec1);

        // Pestaña 2: Productos
        TabHost.TabSpec spec2 = host.newTabSpec("tab2");
        spec2.setIndicator("PRODUCTOS");
        spec2.setContent(R.id.pestaña_producto);
        host.addTab(spec2);

        // Pestaña 3: Compras
        TabHost.TabSpec spec3 = host.newTabSpec("tab3");
        spec3.setIndicator("COMPRAS");
        spec3.setContent(R.id.pestaña_compra);
        host.addTab(spec3);
    }

    private void inicializarControles() {
        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtMarca = findViewById(R.id.txtMarca);
        txtPresentacion = findViewById(R.id.txtPresentacion);
        txtPrecio = findViewById(R.id.txtPrecio);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }

    public void registrarProducto() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "market_db", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String cod = txtCodigo.getText().toString();
        String desc = txtDescripcion.getText().toString();
        String mar = txtMarca.getText().toString();
        String pres = txtPresentacion.getText().toString();
        String prec = txtPrecio.getText().toString();

        if (!cod.isEmpty() && !desc.isEmpty() && !prec.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("Codigo", cod);
            registro.put("Descripcion", desc);
            registro.put("Marca", mar);
            registro.put("Presentacion", pres);
            registro.put("precio", prec);
            registro.put("foto", "");

            long res = db.insert("Informacion", null, registro);
            db.close();

            if (res != -1) {
                limpiarCampos();
                Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Complete los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarProducto() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "market_db", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String cod = txtCodigo.getText().toString();
        if (!cod.isEmpty()) {
            int cant = db.delete("Informacion", "Codigo=" + cod, null);
            db.close();
            limpiarCampos();
            String msj = (cant == 1) ? "Eliminado" : "No existe";
            Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtMarca.setText("");
        txtPresentacion.setText("");
        txtPrecio.setText("");
    }
}






