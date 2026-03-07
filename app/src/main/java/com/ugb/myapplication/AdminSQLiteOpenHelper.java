package com.ugb.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Constructor simplificado para evitar errores de parámetros
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta la creación de la tabla
        db.execSQL("CREATE TABLE Informacion (Codigo INTEGER PRIMARY KEY AUTOINCREMENT, Descripcion TEXT, Marca TEXT, Prsentacion TEXT, Precio REAL)");
        db.execSQL("CREATE TABLE Producto ()")
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se usa para actualizar la estructura de la tabla si el app cambia de versión
        db.execSQL("DROP TABLE IF EXISTS Informacion");
        onCreate(db);
    }
}