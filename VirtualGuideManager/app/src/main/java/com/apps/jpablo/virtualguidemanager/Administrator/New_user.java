package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class New_user extends ActionBarActivity {

    DBContract dataSource;
    Cursor c1 = null;
    String[] listProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_new_user);

        dataSource = new DBContract(this);
    }

    //Función para carga la lista ed proyectos
    public void loadAllProjects() {
        //ArrayList with names of projects
        ArrayList<String> listUsers = new ArrayList<String>();
        //Query
        String query = "Select "+DBContract.ColumnProjects.ID+", "+DBContract.ColumnProjects.NAME+" from "+ DBContract.PROJECTS_TABLE_NAME;
        c1 = dataSource.Select(query,null);
        CharSequence[] items;
        //Adding results of query to arraylist
        c1.moveToFirst();
        do
        {
            listUsers.add(c1.getString(1));
        }
        while(c1.moveToNext());

        String[] simpleArray = new String[ listUsers.size() ];
        listUsers.toArray(simpleArray );
        listProjects = simpleArray;


        //Rellenando el listview
        ArrayAdapter<String> adapter;
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listUsers);
        ListView lv  =(ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
        LinearLayout ll = (LinearLayout) findViewById(R.id.lineaLayout_projects);
        lv.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public class DialogoSeleccion extends DialogFragment {
        private String[] items = null;
        DialogoSeleccion(String[] elementos)
        {
            items = elementos;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setTitle("Selección")
                    .setMultiChoiceItems(items, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                    //Log.i("Dialogos", "Opción elegida: " + items[item]);
                                }
                            });
            /*builder.setTitle("Selección")
                    .setSingleChoiceItems(items, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    //Log.i("Dialogos", "Opción elegida: " + items[item]);
                                }

                            });*/
            builder.setNeutralButton("Terminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return builder.create();
        }
    }

    public void showProjectList(View view)
    {
        loadAllProjects();
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoSeleccion dialogo = new DialogoSeleccion(listProjects);
        dialogo.show(fragmentManager, "tagSeleccion");
    }



}
