package com.apps.jpablo.virtualguidemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JuanPablo on 01/03/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 6;

    public DBHelper(Context context){
        super(context,
                DATABASE_NAME,//String name
                null,//factory
                DATABASE_VERSION//int version
        );

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la tabla Quotes
        db.execSQL(DBContract.CREATE_USERS_SCRIPT);
        //Insertar registros iniciales
        db.execSQL(DBContract.INSERT_USERS_SCRIPT);
        //Crear la tabla Quotes
        db.execSQL(DBContract.CREATE_PROJECTS_SCRIPT);
        //Insertar registros iniciales
        db.execSQL(DBContract.INSERT_PROJECTS_SCRIPT);
        //Crear la tabla Quotes
        db.execSQL(DBContract.CREATE_USERS_PROJ_SCRIPT);
        //Insertar registros iniciales
        db.execSQL(DBContract.INSERT_USER_PROJ_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}