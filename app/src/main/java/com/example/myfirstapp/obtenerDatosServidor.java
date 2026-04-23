package com.example.myfirstapp;

import android.os.Handler;
import android.os.Looper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class obtenerDatosServidor {

    public interface Callback {
        void onRespuesta(String respuesta);
    }

    // Obtener todos los productos (usa url_consulta de utilidades)
    public static void obtener(Callback callback) {
        obtener(utilidades.url_consulta, callback);
    }

    // Obtener un documento específico por URL
    public static void obtener(String _url, Callback callback) {
        new Thread(() -> {
            StringBuilder respuesta = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization",
                        "Basic " + utilidades.credencialesCodificadas);

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String linea;
                while ((linea = bufferedReader.readLine()) != null) respuesta.append(linea);

            } catch (Exception e) {
                respuesta.append("Error: ").append(e.getMessage());
            } finally {
                if (httpURLConnection != null) httpURLConnection.disconnect();
            }

            String finalRespuesta = respuesta.toString();
            new Handler(Looper.getMainLooper()).post(() -> {
                if (callback != null) callback.onRespuesta(finalRespuesta);
            });
        }).start();
    }
}