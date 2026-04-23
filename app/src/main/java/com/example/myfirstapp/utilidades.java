package com.example.myfirstapp;

import android.util.Base64;

public class utilidades {
    static String url_consulta = "http://10.0.2.2:5984/tienda/_design/tienda/_view/tienda";
    static String url_mto = "http://10.0.2.2:5984/tienda";
    static String user = "david";
    static String passwd = "123456";
    static String credencialesCodificadas = Base64.encodeToString(
            (user + ":" + passwd).getBytes(), Base64.NO_WRAP);

    public String generarUnicoId() {
        return java.util.UUID.randomUUID().toString();
    }
}