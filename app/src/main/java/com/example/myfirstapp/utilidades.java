package com.example.myfirstapp;

import android.util.Base64;

public class utilidades {
    static String url_consulta = "http:// http://127.0.0.1/:5984/claudia_sorto/_design/claudia_sorto/_view/jennifer";
    static String url_mto = "http:// http://127.0.0.1/:5984/claudia_sorto";// esto sirve para guardar, actualizar o eliminar
    //es la ruta directa a la bd donde se envian los datos
    static String user = "admin";
    static String passwd = "1234";
    static String credencialesCodificadas = Base64.encodeToString(
            (user + ":" + passwd).getBytes(), Base64.NO_WRAP);

    public String generarUnicoId() {
        return java.util.UUID.randomUUID().toString();
    }
}


