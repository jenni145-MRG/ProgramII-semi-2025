package com.ugb.myapplication;

public class productos {
    String id;
    String descripcion;
    String marca;
    String precio;
    String foto;

    public productos(String id, String descripcion, String marca, String precio, String foto) {
        this.id = id;
        this.descripcion = descripcion;
        this.marca = marca;
        this.precio = precio;
        this.foto = foto;
    }

    public String getDescripcion() { return descripcion; }
    public String getMarca() { return marca; }
    public String getPrecio() { return precio; }
    public String getFoto() { return foto; }
}
