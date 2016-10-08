package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

import com.apps.jpablo.virtualguidemanager.Classes.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class Main_infopoints extends ActionBarActivity {

    DBContract dataSource;
    int id_user;
    Cursor c1 = null;
    private static int save = -1;
    boolean SELECTED_ITEM = false;
    private static final int NEW_POINT = 1;
    private static final int MODIFY_POINT = 2;
    Menu_adm m = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main_infopoints);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);
        //Recuperamos el id de usuario
        id_user = getIntent().getExtras().getInt("id_usuario");
        //Cargamos la lista de usuarios
        loadPoints();
        //Sobrecargamos el LongClick para seleccionar elementos.
        final ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                return onLongListItemClick(lv, pos, id);
            }
        });
        //Inicializamos la clase menu para poder realizar los cambios de menu
        m = new Menu_adm(this,id_user);

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
                startActivity(m.gotoProjectManager());
                return true;
            case R.id.menu_users:
                startActivity(m.gotoUserManager());
                return true;
            case R.id.menu_infopoints:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Función para carga la lista de infopoints
    public void loadPoints() {
        //ArrayList with names of projects
        ArrayList<String> listPoints = new ArrayList<String>();
        //Query
        String query = "Select "+DBContract.ColumnInfopoint.ID+", "+DBContract.ColumnInfopoint.NAME+" from "+ DBContract.INFO_POINT_TABLENAME;
        c1 = dataSource.Select(query,null);
        //Adding results of query to arraylist
        c1.moveToFirst();
        do
        {
            listPoints.add(c1.getString(1));
        }
        while(c1.moveToNext());

        //Rellenando el listview
        ArrayAdapter<String> adapter;
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listPoints);
        ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }

    //Funcion para la seleccion de elementos para modificarlos y eliminarlos
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
            ImageButton ib1 = (ImageButton) findViewById(R.id.fab_deleteinfopoint);
            ib1.setVisibility(View.VISIBLE);
            ImageButton ib2 = (ImageButton) findViewById(R.id.fab_modifyinfopoint);
            ib2.setVisibility(View.VISIBLE);
            ImageButton ib3 = (ImageButton) findViewById(R.id.fab_newinfopoint);
            ib3.setVisibility(View.INVISIBLE);
        }
        else
        {
            ImageButton ib1 = (ImageButton) findViewById(R.id.fab_deleteinfopoint);
            ib1.setVisibility(View.INVISIBLE);
            ImageButton ib2 = (ImageButton) findViewById(R.id.fab_modifyinfopoint);
            ib2.setVisibility(View.INVISIBLE);
            ImageButton ib3 = (ImageButton) findViewById(R.id.fab_newinfopoint);
            ib3.setVisibility(View.VISIBLE);
        }
    }

    //Devuelve el id del usuario seleccionado de la lista
    public int getSelectedItemListView() {
        c1.moveToFirst();
        c1.moveToPosition(save);
        return c1.getInt(0);
    }

    //Clear the seleted items of listview
    public void clearListView()
    {
        loadPoints();
        save = -1;
        SELECTED_ITEM = false;
        showUpdateDelete(SELECTED_ITEM);
    }

    //Sobrecargamos onBackPressed para que en caso de tener un elemento seleccionado lo deseleccione
    @Override
    public void onBackPressed() {
        if(SELECTED_ITEM)
            clearListView();
        else
            finish();
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case NEW_POINT:
                if (resultCode == Activity.RESULT_OK)
                {
                    loadPoints();
                }
                else if(resultCode == Activity.RESULT_CANCELED)
                {
                }
                break;
            case MODIFY_POINT:
                if(resultCode == Activity.RESULT_OK)
                    Toast.makeText(getApplicationContext(), "Element modified successfully.", Toast.LENGTH_SHORT).show();
                clearListView();
                break;
        }
    }

    //FUNCIONES PARA AÑADIR, BORRAR Y ACTUALIZAR UN PROYECTO------------------------------------------
    public void newInfopoint(View view) {
        Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.New_point.class);
        startActivityForResult(intent, NEW_POINT, null);
    }

    public void modifyInfopoint(View view)
    {
        int id_point = getSelectedItemListView();
        Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.Modify_point.class);
        intent.putExtra("id_punto_mod",id_point);
        startActivityForResult(intent, MODIFY_POINT, null);
    }

    public void deletePoint()
    {
        int id = getSelectedItemListView();
        String[] values = {String.valueOf(id)};
        if(dataSource.Delete(DBContract.INFO_POINT_TABLENAME, DBContract.ColumnInfopoint.ID, values)) {
            dataSource.Delete(DBContract.IP_PROJ_TABLE_NAME, DBContract.ColumnIn_proj.ID_INFOPOINT, values);
            clearListView();
        }
        else
            Toast.makeText(getApplicationContext(), "Can't delete the item. An error ocurred", Toast.LENGTH_SHORT).show();
    }

    //Dialogoo de advertencia para comprobar el deseo de eliminar
    public void dialogDeleteInfopoint(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure to delete this user?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePoint();
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


}
