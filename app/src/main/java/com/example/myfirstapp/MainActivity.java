package com.example.myfirstapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
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
    TextView tempVal;
    String accion = "nuevo", idProducto = "", urlFoto = "";
    ArrayList<String> listaFotos = new ArrayList<>();
    int fotoActual = 0;
    Intent tomarFotoIntent;
    FloatingActionButton fab;
    ImageButton img;
    TextView tvContadorFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db              = new DB(this);
        img             = findViewById(R.id.imgFotoProducto);
        tvContadorFotos = findViewById(R.id.tvContadorFotos);
        btn             = findViewById(R.id.btnGuardarProducto);
        fab             = findViewById(R.id.fabListaProductos);

        img.setClipToOutline(true);
        img.setOnClickListener(v -> tomarFoto());

        TextView tvAgregarFoto = findViewById(R.id.tvAgregarFoto);
        tvAgregarFoto.setOnClickListener(v -> tomarFoto());

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

        btn.setOnClickListener(v -> guardarProducto());

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, ListaActivity.class)));

        cargarDatosEdicion();
    }

    private void mostrarFoto(int indice) {
        if (listaFotos.size() > 0 && indice < listaFotos.size()) {
            File foto = new File(listaFotos.get(indice));
            if (foto.exists()) {
                img.setImageURI(null);
                img.setImageURI(Uri.fromFile(foto));
                img.setClipToOutline(true);
            }
            tvContadorFotos.setText((indice + 1) + "/" + listaFotos.size() + " fotos");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnxAgregar) {
            limpiarCampos();
            return true;
        } else if (id == R.id.mnxModificar) {
            guardarProducto();
            return true;
        } else if (id == R.id.mnxEliminar) {
            if (!idProducto.isEmpty()) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Eliminar Producto")
                        .setMessage("¿Estás seguro que deseas eliminar este producto?")
                        .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                            String[] datos = {idProducto, "", "", "", "", "", ""};
                            String resultado = db.administrar_Productos("eliminar", datos);
                            if (resultado.equals("ok")) {
                                eliminarEnCouchDB(idProducto);
                                mostrarMsg("Producto eliminado ✓");
                                limpiarCampos();
                            }
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                mostrarMsg("Selecciona un producto para eliminar");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tomarFoto() {
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File fotoProducto = crearImgProducto();
            if (fotoProducto != null) {
                Uri uriFoto = FileProvider.getUriForFile(
                        MainActivity.this,
                        getPackageName() + ".fileprovider",
                        fotoProducto);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(tomarFotoIntent, 1);
            } else {
                mostrarMsg("No se pudo crear la foto");
            }
        } catch (Exception e) {
            mostrarMsg("Error al tomar la foto: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                listaFotos.add(urlFoto);
                fotoActual = listaFotos.size() - 1;
                mostrarFoto(fotoActual);
            } else {
                mostrarMsg("No fue posible mostrar la foto");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private File crearImgProducto() throws Exception {
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "foto_" + fechaHoraMs;
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (!dirAlmacenamiento.exists()) {
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlFoto = image.getAbsolutePath();
        return image;
    }

    private void guardarProducto() {
        tempVal = findViewById(R.id.txtCodigo);
        String codigo = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtDescripcion);
        String descripcion = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtMarca);
        String marca = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtPresentacion);
        String presentacion = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtPrecio);
        String precio = tempVal.getText().toString();

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            mostrarMsg("Código, descripción y precio son obligatorios");
            return;
        }

        String todasLasFotos = String.join(",", listaFotos);
        String[] datos = {idProducto, codigo, descripcion, marca,
                presentacion, precio, todasLasFotos};
        String resultado = db.administrar_Productos(accion, datos);

        if (resultado.equals("ok")) {
            mostrarMsg("Producto guardado con éxito ✓");

            detectarinternet detector = new detectarinternet(this);
            if (detector.hayConexionInternet()) {
                if (accion.equals("nuevo")) {
                    // Obtener el último ID insertado en SQLite
                    Cursor c = db.lista_productos();
                    String idReal = "";
                    if (c.moveToLast()) idReal = c.getString(0);
                    c.close();
                    sincronizarConCouchDB(idReal, codigo, descripcion,
                            marca, presentacion, precio, todasLasFotos);
                } else {
                    // Modificar — usar el idProducto existente
                    sincronizarConCouchDB(idProducto, codigo, descripcion,
                            marca, presentacion, precio, todasLasFotos);
                }
            } else {
                mostrarMsg("Sin internet, solo guardado localmente");
            }

            limpiarCampos();
        } else {
            mostrarMsg("Error: " + resultado);
        }
    }

    private void sincronizarConCouchDB(String id, String codigo, String descripcion,
                                       String marca, String presentacion,
                                       String precio, String urlFoto) {
        String docUrl = utilidades.url_mto + "/producto_" + id;

        // Primero verificar si ya existe para obtener _rev
        obtenerDatosServidor.obtener(docUrl, respuesta -> {
            String rev = "";
            if (respuesta != null && respuesta.contains("_rev")) {
                try {
                    int i = respuesta.indexOf("\"_rev\":\"") + 8;
                    int j = respuesta.indexOf("\"", i);
                    rev = respuesta.substring(i, j);
                } catch (Exception e) { }
            }

            String revFinal = rev;
            String json = "{" +
                    (revFinal.isEmpty() ? "" : "\"_rev\":\"" + revFinal + "\",") +
                    "\"codigo\":\"" + codigo + "\"," +
                    "\"descripcion\":\"" + descripcion + "\"," +
                    "\"marca\":\"" + marca + "\"," +
                    "\"presentacion\":\"" + presentacion + "\"," +
                    "\"precio\":\"" + precio + "\"," +
                    "\"urlFoto\":\"" + urlFoto + "\"" +
                    "}";

            enviarDatosServidor.enviar(json, "PUT", docUrl, r -> {
                if (r != null && (r.contains("\"ok\":true") || r.contains("\"id\""))) {
                    mostrarMsg("Sincronizado con CouchDB ✓");
                } else {
                    mostrarMsg("Error CouchDB: " + r);
                }
            });
        });
    }

    private void eliminarEnCouchDB(String id) {
        String docUrl = utilidades.url_mto + "/producto_" + id;
        obtenerDatosServidor.obtener(docUrl, respuesta -> {
            if (respuesta != null && respuesta.contains("_rev")) {
                try {
                    int i = respuesta.indexOf("\"_rev\":\"") + 8;
                    int j = respuesta.indexOf("\"", i);
                    String rev = respuesta.substring(i, j);
                    String deleteUrl = docUrl + "?rev=" + rev;
                    enviarDatosServidor.enviar("", "DELETE", deleteUrl, r ->
                            mostrarMsg(r != null && r.contains("\"ok\":true")
                                    ? "Eliminado de CouchDB ✓"
                                    : "Error al eliminar de CouchDB"));
                } catch (Exception e) {
                    mostrarMsg("Error al eliminar de CouchDB");
                }
            }
        });
    }

    private void cargarDatosEdicion() {
        Intent intent = getIntent();
        if (intent.hasExtra("idProducto")) {
            accion     = "modificar";
            idProducto = intent.getStringExtra("idProducto");
            urlFoto    = intent.getStringExtra("urlFoto");

            ((EditText) findViewById(R.id.txtCodigo))
                    .setText(intent.getStringExtra("codigo"));
            ((EditText) findViewById(R.id.txtDescripcion))
                    .setText(intent.getStringExtra("descripcion"));
            ((EditText) findViewById(R.id.txtMarca))
                    .setText(intent.getStringExtra("marca"));
            ((EditText) findViewById(R.id.txtPresentacion))
                    .setText(intent.getStringExtra("presentacion"));
            ((EditText) findViewById(R.id.txtPrecio))
                    .setText(intent.getStringExtra("precio"));

            if (urlFoto != null && !urlFoto.isEmpty()) {
                String[] fotos = urlFoto.split(",");
                for (String f : fotos) listaFotos.add(f.trim());
                fotoActual = 0;
                mostrarFoto(fotoActual);
            }
            btn.setText("Actualizar Producto");
        }
    }

    private void limpiarCampos() {
        ((EditText) findViewById(R.id.txtCodigo)).setText("");
        ((EditText) findViewById(R.id.txtDescripcion)).setText("");
        ((EditText) findViewById(R.id.txtMarca)).setText("");
        ((EditText) findViewById(R.id.txtPresentacion)).setText("");
        ((EditText) findViewById(R.id.txtPrecio)).setText("");
        img.setImageResource(android.R.drawable.ic_menu_camera);
        listaFotos.clear();
        fotoActual  = 0;
        urlFoto     = "";
        accion      = "nuevo";
        idProducto  = "";
        tvContadorFotos.setText("0 fotos");
        btn.setText("Guardar Producto");
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}