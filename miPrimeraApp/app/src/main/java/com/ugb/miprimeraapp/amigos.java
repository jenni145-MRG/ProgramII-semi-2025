package com.ugb.miprimeraapp;

public class amigos {
    String idAmigo;
    String nombre;

    String direccion;

    String correo;

    String telefono;

    String email;

    String dui;

    String foto;

    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public amigos(String foto, String dui, String email, String telefono, String correo, String direccion, String nombre, String idAmigo) {
        this.foto = foto;
        this.dui = dui;
        this.email = email;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.nombre = nombre;
        this.idAmigo = idAmigo;
    }
}
