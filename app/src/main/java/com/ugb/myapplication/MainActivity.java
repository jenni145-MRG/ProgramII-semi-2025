package com.ugb.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {



    @SuppressLint("MissingSuperCall")

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TabHost host = findViewById(R.id.miTabHost);
            host.setup();

            TabHost.TabSpec spec1 = host.newTabSpec("tab1");
            spec1.setIndicator("REGISTRO");
            spec1.setContent(R.id.pestaña_registro);
            host.addTab(spec1);

            TabHost.TabSpec spec2 = host.newTabSpec("tab2");
            spec2.setIndicator("PRODUCTOS");
            spec2.setContent(R.id.pestaña_producto);
            host.addTab(spec2);

        TabHost.TabSpec spec3 = host.newTabSpec("tab3");
        spec3.setIndicator("COMPRAS");
        spec3.setContent(R.id.pestaña_compra);
        host.addTab(spec3);




    }
    }






