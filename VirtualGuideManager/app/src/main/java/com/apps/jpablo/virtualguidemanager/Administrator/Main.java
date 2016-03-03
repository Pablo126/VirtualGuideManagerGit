package com.apps.jpablo.virtualguidemanager.Administrator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

public class Main extends ActionBarActivity {

    DBContract dataSource;
    int id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);

        //Recuperamos el id de usuario
        id_user = getIntent().getExtras().getInt("id_usuario");

        loadProjects();
    }

    public void loadProjects() {

        String nombre;
        //Si existe el usuario
        String query = "Select "+DBContract.PROJECTS_TABLE_NAME+"."+DBContract.ColumnProjects.ID+", "+DBContract.PROJECTS_TABLE_NAME+"."+
                DBContract.ColumnProjects.NAME+" from "+
                DBContract.USER_PROJ_TABLE_NAME+", "+DBContract.PROJECTS_TABLE_NAME+" where "+DBContract.USER_PROJ_TABLE_NAME+"."+
                DBContract.ColumnUser_proj.ID_USER+" = "+id_user
                +" and "+DBContract.PROJECTS_TABLE_NAME+"."+DBContract.ColumnProjects.ID+" = "+DBContract.USER_PROJ_TABLE_NAME+"."+DBContract.ColumnUser_proj.ID_PROJECT;
        Cursor c1 = dataSource.Select(query);
        while(c1.moveToNext())
        {
            nombre = c1.getString(1);
        }
    }

}
