package com.ugb.myapplication;

import android.os.Bundle;
import android.widget.TabHost;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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









