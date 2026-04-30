package com.example.myfirstapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DB db;
    Button btn;
    String accion = "nuevo", idProducto = "", urlFoto = "";
    ArrayList<String> listaFotos = new ArrayList<>();
    int fotoActual = 0;
    ImageButton img;
    TextView tvContadorFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        img = findViewById(R.id.imgFotoProducto);
        btn = findViewById(R.id.btnGuardarProducto);
        tvContadorFotos = findViewById(R.id.tvAgregarFoto); // Asegúrate que el ID coincida

        img.setOnClickListener(v -> tomarFoto());
        btn.setOnClickListener(v -> guardarProducto());

        findViewById(R.id.fabListaProductos).setOnClickListener(v ->
                startActivity(new Intent(this, ListaActivity.class)));

        findViewById(R.id.btnFotoAnterior).setOnClickListener(v -> {
            if (listaFotos.size() > 0) {
                fotoActual = (fotoActual - 1 + listaFotos.size()) % listaFotos.size();
                mostrarFoto(fotoActual);
            }
        });

        findViewById(R.id.btnFotoSiguiente).setOnClickListener(v -> {
            if (listaFotos.size() > 0) {
                fotoActual = (fotoActual + 1) % listaFotos.size();
                mostrarFoto(fotoActual);
            }
        });

        cargarDatosEdicion();
    }

    private void mostrarFoto(int indice) {
        if (listaFotos.size() > 0 && indice < listaFotos.size()) {
            File foto = new File(listaFotos.get(indice));
            if (foto.exists()) {
                img.setImageURI(null);
                img.setImageURI(Uri.fromFile(foto));
            }
        }
    }

    private void tomarFoto() {
        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File fotoProducto = crearImgProducto();
            if (fotoProducto != null) {
                Uri uriFoto = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", fotoProducto);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(tomarFotoIntent, 1);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            listaFotos.add(urlFoto);
            fotoActual = listaFotos.size() - 1;
            mostrarFoto(fotoActual);
        }
    }

    private File crearImgProducto() throws Exception {
        String fileName = "foto_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(fileName, ".jpg", dir);
        urlFoto = image.getAbsolutePath();
        return image;
    }

    private void guardarProducto() {
        String codigo = ((EditText) findViewById(R.id.txtCodigo)).getText().toString();
        String descripcion = ((EditText) findViewById(R.id.txtDescripcion)).getText().toString();
        String precio = ((EditText) findViewById(R.id.txtPrecio)).getText().toString();
        String costo = ((EditText) findViewById(R.id.txtCosto)).getText().toString();
        String stock = ((EditText) findViewById(R.id.txtStock)).getText().toString();

        if (codigo.isEmpty() || precio.isEmpty() || costo.isEmpty()) {
            mostrarMsg("Campos obligatorios vacíos");
            return;
        }

        String todasLasFotos = String.join(",", listaFotos);
        String ganancia = String.valueOf(Double.parseDouble(precio) - Double.parseDouble(costo));

        String[] datos = {idProducto, codigo, descripcion, "", "", precio, todasLasFotos, costo, ganancia, stock};
        String resultado = db.administrar_Productos(accion, datos);

        if (resultado.equals("ok")) {
            if (new detectarinternet(this).hayConexionInternet()) {
                // SI ES NUEVO, BUSCAMOS EL ID QUE SQLITE ACABA DE GENERAR
                String idParaSincronizar = idProducto;
                if (accion.equals("nuevo")) {
                    Cursor cur = db.lista_productos();
                    if (cur.moveToLast()) idParaSincronizar = cur.getString(0);
                    cur.close();
                }
                sincronizarConCouchDB(idParaSincronizar, codigo, descripcion, precio, costo, ganancia, stock, todasLasFotos);
            }
            mostrarMsg("Guardado ✓");
            finish();
        }
    }

    private void sincronizarConCouchDB(String id, String cod, String des, String pve, String cos, String gan, String sto, String foto) {
        String docUrl = utilidades.url_mto + "/" + id; // USAMOS EL ID QUE PASAMOS POR PARÁMETRO

        obtenerDatosServidor.obtener(docUrl, respuesta -> {
            String rev = "";
            if (respuesta != null && respuesta.contains("_rev")) {
                try {
                    int i = respuesta.indexOf("\"_rev\":\"") + 8;
                    int j = respuesta.indexOf("\"", i);
                    rev = respuesta.substring(i, j);
                } catch (Exception e) {}
            }

            String json = "{";
            if (!rev.isEmpty()) json += "\"_rev\":\"" + rev + "\",";
            json += "\"idProducto\":\"" + id + "\"," +
                    "\"codigo\":\"" + cod + "\"," +
                    "\"descripcion\":\"" + des + "\"," +
                    "\"precio\":" + pve + "," +
                    "\"costo\":" + cos + "," +
                    "\"ganancia\":" + gan + "," +
                    "\"stock\":" + sto + "," +
                    "\"urlFoto\":\"" + foto + "\"" +
                    "}";

            enviarDatosServidor.enviar(json, "PUT", docUrl, r -> {
                android.util.Log.d("COUCHDB", r);
            });
        });
    }

    private void cargarDatosEdicion() {
        Intent intent = getIntent();
        if (intent.hasExtra("idProducto")) {
            accion = "modificar";
            idProducto = intent.getStringExtra("idProducto");
            ((EditText) findViewById(R.id.txtCodigo)).setText(intent.getStringExtra("codigo"));
            ((EditText) findViewById(R.id.txtDescripcion)).setText(intent.getStringExtra("descripcion"));
            ((EditText) findViewById(R.id.txtPrecio)).setText(intent.getStringExtra("precio"));
            ((EditText) findViewById(R.id.txtCosto)).setText(intent.getStringExtra("costo"));
            ((EditText) findViewById(R.id.txtStock)).setText(intent.getStringExtra("stock"));

            String fotos = intent.getStringExtra("urlFoto");
            if (fotos != null && !fotos.isEmpty()) {
                for (String f : fotos.split(",")) listaFotos.add(f.trim());
                mostrarFoto(0);
            }
            btn.setText("Actualizar Producto");
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}