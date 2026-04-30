package com.example.myfirstapp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class enviarDatosServidor {

    public interface Callback {
        void onRespuesta(String respuesta);
    }

    public static void enviar(String jsonDatos, String metodo, String _url, Callback callback) {
        new Thread(() -> {
            String jsonResponse = "";
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod(metodo);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Authorization",
                        "Basic " + utilidades.credencialesCodificadas);


                Writer writer = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                writer.write(jsonDatos);
                writer.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = bufferedReader.readLine()) != null) sb.append(linea);
                jsonResponse = sb.toString();

            } catch (Exception e) {
                jsonResponse = "Error: " + e.getMessage();
            } finally {
                if (httpURLConnection != null) httpURLConnection.disconnect();
            }


            final String finalResponse = jsonResponse;
            new Handler(Looper.getMainLooper()).post(() -> {

                Log.d("COUCHDB_RESPUESTA", "Respuesta del servidor: " + finalResponse);

                if (callback != null) callback.onRespuesta(finalResponse);
            });
        }).start();
    }
}