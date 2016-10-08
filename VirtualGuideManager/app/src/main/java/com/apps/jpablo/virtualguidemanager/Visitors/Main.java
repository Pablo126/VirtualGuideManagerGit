package com.apps.jpablo.virtualguidemanager.Visitors;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apps.jpablo.virtualguidemanager.Classes.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class Main extends ActionBarActivity {

    int id_user;
    Cursor c1 = null;
    DBContract dataSource;
    ListView lv;

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

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        onListItemClick(lv,position,id);
                    }
                }
        );
    }

    protected void onListItemClick(ListView lv, final int pos,long id)
    {
        int id_proj = getSelectedItemListView(pos);
        Intent intent = new Intent(this,com.apps.jpablo.virtualguidemanager.Visitors.Project_selected.class);
        intent.putExtra("id_project", id_proj);
        startActivity(intent);
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
        lv  =(ListView)findViewById(R.id.listViewVisitors);
        lv.setAdapter(adapter);
    }


    //Devuelve el id del proyecto seleccionado
    public int getSelectedItemListView(int pos) {
        c1.moveToFirst();
        c1.moveToPosition(pos);
        return c1.getInt(0);
    }

}
