package com.apps.jpablo.virtualguidemanager.Visitors;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class Main extends ActionBarActivity {

    int id_user;
    Cursor c1 = null;
    DBContract dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_main);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);
        //Recuperamos el id de usuario
        id_user = getIntent().getExtras().getInt("id_usuario");
        //Cargamos los proyectos
        loadProjects();
    }

    //Función para carga la lista ed proyectos
    public void loadProjects() {
        //ArrayList with names of projects
        ArrayList<String> listProjects = new ArrayList<String>();
        //Query
        String query = "Select P."+ DBContract.ColumnProjects.ID+", P."+DBContract.ColumnProjects.NAME+" from "+
                DBContract.USER_PROJ_TABLE_NAME+" UP, "+DBContract.PROJECTS_TABLE_NAME+" P where UP."+DBContract.ColumnUser_proj.ID_USER+"="+id_user+
                " and P."+DBContract.ColumnProjects.ID+" = UP."+DBContract.ColumnUser_proj.ID_PROJECT;
        c1 = dataSource.Select(query,null);
        //Adding results of query to arraylist
        c1.moveToFirst();
        do
        {
            listProjects.add(c1.getString(1));
        }
        while(c1.moveToNext());

        //Rellenando el listview
        ArrayAdapter<String> adapter;
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listProjects);
        ListView lv  =(ListView)findViewById(R.id.listViewVisitors);
        lv.setAdapter(adapter);
    }

}
