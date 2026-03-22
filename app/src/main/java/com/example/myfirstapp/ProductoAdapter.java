package com.example.myfirstapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductoAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> datos;

    public ProductoAdapter(Context context, ArrayList<HashMap<String, String>> datos) {
        this.context = context;
        this.datos = datos != null ? datos : new ArrayList<>();
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_producto, parent, false);
        }

        HashMap<String, String> item = datos.get(position);

        TextView tvCodigo = convertView.findViewById(R.id.tvCodigo);
        TextView tvDescripcion = convertView.findViewById(R.id.tvDescripcion);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecio);
        ImageView imgItem = convertView.findViewById(R.id.imgItem);

        tvCodigo.setText(item.get("codigo"));
        tvDescripcion.setText(item.get("descripcion"));
        tvPrecio.setText("$" + item.get("precio"));

        String urlFoto = item.get("urlFoto");
        imgItem.setImageResource(R.drawable.camera_6286759); // imagen por defecto

        if (urlFoto != null && !urlFoto.isEmpty()) {
            String primeraFoto = urlFoto.split(",")[0].trim();
            if (!primeraFoto.isEmpty()) {
                File foto = new File(primeraFoto);
                if (foto.exists()) {
                    imgItem.setImageURI(null);
                    imgItem.setImageURI(Uri.fromFile(foto));
                    imgItem.setClipToOutline(true);
                }
            }
        }

        return convertView;
    }
}

