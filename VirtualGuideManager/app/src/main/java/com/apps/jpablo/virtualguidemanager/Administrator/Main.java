package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;
import java.util.ArrayList;

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

        ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                return onLongListItemClick(v,pos,id);
            }
            protected boolean onLongListItemClick(View v, final int pos, long id) {
                /////Display Dialog Here.......................
                
                showUpdateDelete();
                return true;
            }

            });



    }

    private void showUpdateDelete()
    {
        ImageButton ib1 = (ImageButton)findViewById(R.id.fab2);
        ib1.setVisibility(View.VISIBLE);
        ImageButton ib2 = (ImageButton)findViewById(R.id.fab3);
        ib2.setVisibility(View.VISIBLE);
        ImageButton ib3 = (ImageButton)findViewById(R.id.fab);
        ib3.setVisibility(View.INVISIBLE);
    }

    private void dialogDeleteModify()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.proj_del_up);
        builder.setPositiveButton(R.string.proj_del, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.proj_up,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    private static final int NEW_PROJECT = 1;

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case NEW_PROJECT:
                if (resultCode == Activity.RESULT_OK) {
                    loadProjects();
                }
                else if(resultCode == Activity.RESULT_CANCELED)
                {

                }

                break;

        }
    }



    public void loadProjects() {
        //ArrayList with names of projects
        ArrayList<String> listProjects = new ArrayList<String>();
        //Query
        String query = "Select P."+DBContract.ColumnProjects.ID+", P."+DBContract.ColumnProjects.NAME+" from "+
                DBContract.USER_PROJ_TABLE_NAME+" UP, "+DBContract.PROJECTS_TABLE_NAME+" P where P."+DBContract.ColumnProjects.ID+" = UP."+DBContract.ColumnUser_proj.ID_PROJECT;
        Cursor c1 = dataSource.Select(query,null);
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
        ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }

    public void newProject(View view) {
        Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.New_project.class);
        startActivityForResult(intent,NEW_PROJECT,null);
    }

}
