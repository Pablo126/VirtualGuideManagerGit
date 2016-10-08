package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.Classes.DBContract;
import com.apps.jpablo.virtualguidemanager.Classes.Infopoint;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class Modify_project extends ActionBarActivity {

    DBContract dataSource;
    int id_project;
    TextView tv_name, tv_description, tv_background;

    Cursor c1 = null;
    String[] listInfopointsString;
    int[] listInfopointsCheckedID;
    boolean[] listInfopointsChecked;
    //ArrayList with names of projects
    ArrayList<Infopoint> listInfopoints = new ArrayList<Infopoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_modify_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);
        //Recuperamos el id de usuario
        id_project = getIntent().getExtras().getInt("id_project");

        //Hacemos una consulta a database para cargar los datos
        Cursor c = dataSource.Select("Select * from "+DBContract.PROJECTS_TABLE_NAME+" where "+DBContract.ColumnProjects.ID+"="+id_project,null);
        if(c.moveToFirst())
        {
            tv_name = (TextView) findViewById(R.id.etName);
            tv_description = (TextView) findViewById(R.id.etDescription);
            tv_background = (TextView) findViewById(R.id.etBackground);
            tv_name.setText(c.getString(1));
            tv_description.setText(c.getString(2));
            tv_background.setText(c.getString(3));
        }

        loadAllInfopoints();
        loadInfopointsLinked();
    }

    //Función para carga la lista ed infopoints
    public void loadAllInfopoints() {
        //Query todos los infopoints
        String query = "Select "+ DBContract.ColumnInfopoint.ID+", "+DBContract.ColumnInfopoint.NAME+" from "+ DBContract.INFO_POINT_TABLENAME;
        c1 = dataSource.Select(query, null);
        //Adding results of query to arraylist
        if(c1.moveToFirst()) {
            do {
                listInfopoints.add(new Infopoint(c1.getInt(0), c1.getString(1), false));
            }
            while (c1.moveToNext());

            String[] ArrayStringProj = new String[listInfopoints.size()];
            ArrayList<String> aux = new ArrayList<String>();
            for (Infopoint p : listInfopoints)
                aux.add(p.getName());
            aux.toArray(ArrayStringProj);
            listInfopointsString = ArrayStringProj;
        }
    }

    private void loadInfopointsLinked()
    {
        Cursor c2 = dataSource.Select("Select * from "+DBContract.IP_PROJ_TABLE_NAME+" where "+DBContract.ColumnIn_proj.ID_PROJECT+"="+id_project,null);
        if(c2.moveToFirst()) {
            do {
                for (Infopoint p : listInfopoints) {

                    if (p.getID() == c2.getInt(0))
                        p.setSelected(true);
                }
            }
            while (c2.moveToNext());
            refreshListInfopointsSelected();
        }
    }

    public void refreshListInfopointsSelected()
    {
        String projectsSelected = "";
        boolean[] aux = new boolean[listInfopoints.size()];
        int[] aux2 = new int[listInfopoints.size()];
        int count=0;
        for(Infopoint p : listInfopoints)
        {
            aux[count] = p.getSelected();
            if(p.getSelected())
            {
                projectsSelected += (p.getName() + ", ");
                aux2[count] = p.getID();
            }
            count++;
        }
        listInfopointsChecked = aux;
        listInfopointsCheckedID = aux2;
        if(!projectsSelected.isEmpty())
            projectsSelected = projectsSelected.substring(0,projectsSelected.length()-2)+".";
        TextView tv = (TextView) findViewById(R.id.tvlistInfopoints);
        tv.setText(projectsSelected);
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            Intent resultado = new Intent();
            setResult(RESULT_CANCELED, resultado);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateProject(View view)
    {
        if(dataSource.UpdateProject(id_project,tv_name.getText().toString(),tv_description.getText().toString(),tv_background.getText().toString()))
        {
            //Eliminamos los proyectos anteriormente enlazados a este usuario
            String[] values = {String.valueOf(id_project)};
            if(dataSource.Delete(DBContract.IP_PROJ_TABLE_NAME, DBContract.ColumnIn_proj.ID_PROJECT, values))
            {
                int limite = 0;
                if(listInfopointsCheckedID!=null)
                    limite = listInfopointsCheckedID.length;
                //Recorremos la lista de proyectos asociados al nuevo usuario.
                for (int i = 0; i < limite; i++) {
                    if (listInfopointsCheckedID[i] != -0) {
                        dataSource.InsertInfoProj(listInfopointsCheckedID[i],id_project);
                    }
                }
            }
            Intent resultado = new Intent();
            setResult(RESULT_OK, resultado);
            finish();
        }
        else
        {
            Intent resultado = new Intent();
            setResult(RESULT_CANCELED, resultado);
            finish();
        }
    }

    public void showProjectList(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoSeleccion dialogo = new DialogoSeleccion(listInfopointsString);
        dialogo.show(fragmentManager, "tagSeleccion");
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
                    .setMultiChoiceItems(items, listInfopointsChecked,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                    listInfopoints.get(item).setSelected(isChecked);
                                    //Log.i("Dialogos", "Opción elegida: " + items[item]);
                                }
                            });
            builder.setNeutralButton("Terminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refreshListInfopointsSelected();
                }
            });

            return builder.create();
        }
    }

    //----------------------FILES------------------------
    public void openFolder(View view)
    {
        String folderPath = Environment.getExternalStorageDirectory()+"/VirtualGuideContent";
        Uri myUri = Uri.parse(folderPath);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(myUri, "file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //startActivityForResult(intent, 1);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 0);

        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void  onActivityResult(int requestCode, int resultCode, Intent intent){

        switch (requestCode) {
            case 0:
            {
                if (resultCode == RESULT_OK) {
                    String contents = intent.getData().getLastPathSegment();
                    TextView tv = (TextView) findViewById(R.id.etBackground);
                    tv.setText(contents);
                }
                break;
            }
        }
    }



}
