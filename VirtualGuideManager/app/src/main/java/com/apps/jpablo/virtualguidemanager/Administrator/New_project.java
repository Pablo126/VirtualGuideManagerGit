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
import android.view.View;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.Infopoint;
import com.apps.jpablo.virtualguidemanager.Project;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class New_project extends ActionBarActivity {

    DBContract dataSource;
    Cursor c1 = null;
    String[] listInfopointsString;
    int[] listInfopointsCheckedID;
    boolean[] listInfopointsChecked;
    //ArrayList with names of projects
    ArrayList<Infopoint> listInfopoints = new ArrayList<Infopoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_new_project);

        dataSource = new DBContract(this);

        loadAllInfopoints();
    }


    public void save(View view)
    {
        //Obtenemos los valores de los campos
        TextView tv_name = (TextView) findViewById(R.id.etName);
        TextView tv_description = (TextView) findViewById(R.id.etDescription);
        TextView tv_type = (TextView) findViewById(R.id.etType);
        TextView tv_background = (TextView) findViewById(R.id.etBackground);
        //Llamamos a la función de insercción en base de datos
        if(newProject(tv_name.getText().toString(),tv_description.getText().toString(),Integer.parseInt(tv_type.getText().toString()),tv_background.getText().toString()))
        {
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
                    refreshListProjectSelected();
                }
            });

            return builder.create();
        }
    }

    public void refreshListProjectSelected()
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

    public void showInfopointList(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoSeleccion dialogo = new DialogoSeleccion(listInfopointsString);
        dialogo.show(fragmentManager, "tagSeleccion");
    }

    private boolean newProject(String name, String description, int type, String background_path)
    {
        int id_nuevo_proyecto = dataSource.InsertProject(name,description, type, background_path);
        if(id_nuevo_proyecto != -1)
        {
            int limit = 0;
            if(listInfopointsCheckedID!= null)
                limit = listInfopointsCheckedID.length;
            //Recorremos la lista de proyectos asociados al nuevo usuario.
            for(int i=0;i<limit;i++)
            {
                if(listInfopointsCheckedID[i]!=-0)
                {
                    dataSource.InsertInfoProj(listInfopointsCheckedID[i], id_nuevo_proyecto);
                }
            }
            return true;
        }
        return false;
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
