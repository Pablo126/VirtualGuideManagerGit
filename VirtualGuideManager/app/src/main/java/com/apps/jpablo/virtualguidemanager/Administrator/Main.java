package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static int save = -1;
    boolean SELECTED_ITEM = false;
    private static final int NEW_PROJECT = 1;
    private static final int MODIFY_PROJECT = 2;
    Cursor c1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);
        //Recuperamos el id de usuario
        id_user = getIntent().getExtras().getInt("id_usuario");
        //Cargamos los proyectos
        loadProjects();
    //Sobrecargamos el LongClick para seleccionar elementos.
        final ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                return onLongListItemClick(lv, pos, id);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_projects:
                return true;
            case R.id.menu_users:
                gotoUserManager();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void gotoUserManager()
    {
        Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.Main_users.class);
        intent.putExtra("id_usuario",id_user);
        //startActivityForResult(intent, MODIFY_PROJECT, null);
        startActivity(intent);
    }

    protected boolean onLongListItemClick(ListView v2, final int pos, long id) {
        /////Display Dialog Here.......................
        if(pos == save && SELECTED_ITEM == false) {
            v2.getChildAt(save).setBackgroundColor(Color.WHITE);
            SELECTED_ITEM = false;
        }
        else {
            for(int i=0; i<v2.getChildCount();i++)
                v2.getChildAt(i).setBackgroundColor(Color.WHITE);
            SELECTED_ITEM = false;
            if(pos != save) {
                v2.getChildAt(pos).setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
                SELECTED_ITEM = true;
                save = pos;
            }
            else
                save = -1;
        }

        showUpdateDelete(SELECTED_ITEM);
        return true;
    }

    //Función para habilitar o deshabilitar botones flotantes de acciones.
    private void showUpdateDelete(boolean selected)
    {
        if(selected) {
            ImageButton ib1 = (ImageButton) findViewById(R.id.fab2);
            ib1.setVisibility(View.VISIBLE);
            ImageButton ib2 = (ImageButton) findViewById(R.id.fab3);
            ib2.setVisibility(View.VISIBLE);
            ImageButton ib3 = (ImageButton) findViewById(R.id.fab);
            ib3.setVisibility(View.INVISIBLE);
        }
        else
        {
            ImageButton ib1 = (ImageButton) findViewById(R.id.fab2);
            ib1.setVisibility(View.INVISIBLE);
            ImageButton ib2 = (ImageButton) findViewById(R.id.fab3);
            ib2.setVisibility(View.INVISIBLE);
            ImageButton ib3 = (ImageButton) findViewById(R.id.fab);
            ib3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case NEW_PROJECT:
                if (resultCode == Activity.RESULT_OK)
                {
                    loadProjects();
                }
                else if(resultCode == Activity.RESULT_CANCELED)
                {
                }
                break;
            case MODIFY_PROJECT:
                if(resultCode == Activity.RESULT_OK)
                    Toast.makeText(getApplicationContext(), "Element modified successfully.", Toast.LENGTH_SHORT).show();
                clearListView();
                break;
        }
    }


    //Función para carga la lista ed proyectos
    public void loadProjects() {
        //ArrayList with names of projects
        ArrayList<String> listProjects = new ArrayList<String>();
        //Query
        String query = "Select P."+DBContract.ColumnProjects.ID+", P."+DBContract.ColumnProjects.NAME+" from "+
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
        ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }

    //FUNCIONES PARA AÑADIR, BORRAR Y ACTUALIZAR UN PROYECTO------------------------------------------
    public void newProject(View view) {
        Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.New_project.class);
        startActivityForResult(intent, NEW_PROJECT, null);
    }

    public void deleteProject()
    {
        int id = getSelectedItemListView();
        String[] values = {String.valueOf(id)};
        if(dataSource.Delete(DBContract.PROJECTS_TABLE_NAME, DBContract.ColumnProjects.ID, values))
            clearListView();
        else
            Toast.makeText(getApplicationContext(), "Can't delete the item. An error ocurred", Toast.LENGTH_SHORT).show();
    }

    public void modifyProject(View view)
    {
        int id_proj = getSelectedItemListView();
        Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.Modify_project.class);
        intent.putExtra("id_project",id_proj);
        startActivityForResult(intent, MODIFY_PROJECT, null);

    }

    public void dialogDelete(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure to delete this project?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProject();
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    //-------------------------------------------------------------------------------------------------

    //Devuelve el id del proyecto seleccionado
    public int getSelectedItemListView() {
        c1.moveToFirst();
        c1.moveToPosition(save);
        return c1.getInt(0);
    }

    //Clear the seleted items of listview
    public void clearListView()
    {
        loadProjects();
        save = -1;
        SELECTED_ITEM = false;
        showUpdateDelete(SELECTED_ITEM);
    }

    @Override
    public void onBackPressed() {
        if(SELECTED_ITEM)
            clearListView();
        else
            finish();
    }

}
