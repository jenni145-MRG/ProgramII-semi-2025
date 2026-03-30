package com.ugb.miprimeraapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class lista_amigos extends AppCompatActivity {
    floatingActionButton fab;
    Bundle parametros = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_amigos);

        fab = findViewById(R.id.fabAgregarAmigos);
        fab.setOnClickListener(v->abrirActivity());

    }
    private void abrirActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(parametros);
        startActivity(intent);


    }
}