package com.example.myfirstapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaActivity extends AppCompatActivity {

    DB db;
    ListView lista;
    EditText txtBuscar;
    ArrayList<HashMap<String, String>> todosLosDatos;
    ProductoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        db        = new DB(this);
        lista     = findViewById(R.id.lvProductos);
        txtBuscar = findViewById(R.id.txtBuscar);

        cargarLista();

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
        });

        lista.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> item =
                    (HashMap<String, String>) parent.getItemAtPosition(position);

            String[] opciones = {"✏️ Modificar", "🗑️ Eliminar", "➕ Agregar nuevo"};

            new AlertDialog.Builder(this)
                    .setTitle(item.get("descripcion"))
                    .setItems(opciones, (dialog, which) -> {
                        switch (which) {

                            case 0:
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra("idProducto",   item.get("idProducto"));
                                intent.putExtra("codigo",       item.get("codigo"));
                                intent.putExtra("descripcion",  item.get("descripcion"));
                                intent.putExtra("marca",        item.get("marca"));
                                intent.putExtra("presentacion", item.get("presentacion"));
                                intent.putExtra("precio",       item.get("precio"));
                                intent.putExtra("urlFoto",      item.get("urlFoto"));
                                startActivity(intent);
                                break;

                            case 1:
                                new AlertDialog.Builder(this)
                                        .setTitle("Eliminar Producto")
                                        .setMessage("¿Estás seguro que deseas eliminar \""
                                                + item.get("descripcion") + "\"?")
                                        .setPositiveButton("Sí, eliminar", (d, w) -> {
                                            String[] datos = {item.get("idProducto"),
                                                    "", "", "", "", "", ""};
                                            String resultado =
                                                    db.administrar_Productos("eliminar", datos);
                                            if (resultado.equals("ok")) {
                                                // Eliminar también en CouchDB
                                                String docUrl = utilidades.url_mto
                                                        + "/producto_" + item.get("idProducto");
                                                obtenerDatosServidor.obtener(docUrl, respuesta -> {
                                                    if (respuesta != null && respuesta.contains("_rev")) {
                                                        try {
                                                            int i = respuesta.indexOf("\"_rev\":\"") + 8;
                                                            int j = respuesta.indexOf("\"", i);
                                                            String rev = respuesta.substring(i, j);
                                                            String deleteUrl = docUrl + "?rev=" + rev;
                                                            enviarDatosServidor.enviar("", "DELETE",
                                                                    deleteUrl, r ->
                                                                            Toast.makeText(this,
                                                                                    r != null && r.contains("\"ok\":true")
                                                                                            ? "Eliminado de CouchDB ✓"
                                                                                            : "Eliminado localmente",
                                                                                    Toast.LENGTH_SHORT).show());
                                                        } catch (Exception e) { }
                                                    }
                                                });
                                                Toast.makeText(this, "Producto eliminado ✓",
                                                        Toast.LENGTH_SHORT).show();
                                                cargarLista();
                                            }
                                        })
                                        .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                                        .show();
                                break;

                            case 2:
                                Intent intentNuevo = new Intent(this, MainActivity.class);
                                startActivity(intentNuevo);
                                break;
                        }
                    })
                    .show();
        });
    }

    private void filtrar(String texto) {
        ArrayList<HashMap<String, String>> filtrados = new ArrayList<>();

        for (HashMap<String, String> fila : todosLosDatos) {
            String codigo      = fila.get("codigo")      != null ? fila.get("codigo").toLowerCase()      : "";
            String descripcion = fila.get("descripcion") != null ? fila.get("descripcion").toLowerCase() : "";
            String precio      = fila.get("precio")      != null ? fila.get("precio").toLowerCase()      : "";

            if (codigo.contains(texto.toLowerCase())      ||
                    descripcion.contains(texto.toLowerCase()) ||
                    precio.contains(texto.toLowerCase())) {
                filtrados.add(fila);
            }
        }
        adapter = new ProductoAdapter(this, filtrados);
        lista.setAdapter(adapter);
    }

    private void cargarLista() {
        todosLosDatos = new ArrayList<>();
        Cursor c = db.lista_productos();

        while (c.moveToNext()) {
            HashMap<String, String> fila = new HashMap<>();
            fila.put("idProducto",   c.getString(0));
            fila.put("codigo",       c.getString(1));
            fila.put("descripcion",  c.getString(2));
            fila.put("marca",        c.getString(3));
            fila.put("presentacion", c.getString(4));
            fila.put("precio",       c.getString(5));
            fila.put("urlFoto",      c.getString(6));
            todosLosDatos.add(fila);
        }
        c.close();

        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        adapter = new ProductoAdapter(this, todosLosDatos);
        lista.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }
}