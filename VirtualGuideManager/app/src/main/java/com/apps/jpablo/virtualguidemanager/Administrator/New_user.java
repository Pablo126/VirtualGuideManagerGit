package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.Classes.DBContract;
import com.apps.jpablo.virtualguidemanager.Classes.File_type;
import com.apps.jpablo.virtualguidemanager.Classes.Project;
import com.apps.jpablo.virtualguidemanager.Classes.User_type;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class New_user extends ActionBarActivity {

    DBContract dataSource;
    Cursor c1 = null;
    String[] listProjectsString;
    int[] listProjectCheckedID;
    boolean[] listProjectChecked;
    //ArrayList with names of projects
    ArrayList<Project> listProjects = new ArrayList<Project>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_new_user);

        dataSource = new DBContract(this);
        //Rellenamos el spinner con los tipos de usuario
        Spinner spinner = (Spinner) findViewById(R.id.tvUserType);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item);
        List<User_type> Users = Arrays.asList(User_type.values());
        List<String> combo = new ArrayList<String>();
        for(User_type u : Users){
            combo.add(u.getName(this.getResources()));}
        adapter.addAll(combo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        loadAllProjects();
    }

    //Función para carga la lista ed proyectos
    public void loadAllProjects() {
        //Query todos los proyectos
        String query = "Select "+DBContract.ColumnProjects.ID+", "+DBContract.ColumnProjects.NAME+" from "+ DBContract.PROJECTS_TABLE_NAME;
        c1 = dataSource.Select(query,null);
        //Adding results of query to arraylist
        c1.moveToFirst();
        do
        {
            listProjects.add(new Project(c1.getInt(0),c1.getString(1),false));
        }
        while(c1.moveToNext());

        String[] ArrayStringProj = new String[listProjects.size()];
        ArrayList<String> aux = new ArrayList<String>();
        for(Project p : listProjects)
            aux.add(p.getName());
        aux.toArray(ArrayStringProj);
        listProjectsString = ArrayStringProj;


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
                    .setMultiChoiceItems(items, listProjectChecked,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                    listProjects.get(item).setSelected(isChecked);
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
        boolean[] aux = new boolean[listProjects.size()];
        int[] aux2 = new int[listProjects.size()];
        int count=0;
        for(Project p : listProjects)
        {
            aux[count] = p.getSelected();
            if(p.getSelected())
            {
                projectsSelected += (p.getName() + ", ");
                aux2[count] = p.getID();
            }
            count++;
        }
        listProjectChecked = aux;
        listProjectCheckedID = aux2;
        if(!projectsSelected.isEmpty())
            projectsSelected = projectsSelected.substring(0,projectsSelected.length()-2)+".";
        TextView tv = (TextView) findViewById(R.id.tvlistProjects);
        tv.setText(projectsSelected);
    }

    public void showProjectList(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoSeleccion dialogo = new DialogoSeleccion(listProjectsString);
        dialogo.show(fragmentManager, "tagSeleccion");
    }

    public void SaveUser(View view)
    {
        try
        {
            EditText etUsername = (EditText) findViewById(R.id.etUsername);
            EditText etPassword = (EditText) findViewById(R.id.etUserPassword);
            Spinner etType = (Spinner) findViewById(R.id.tvUserType);
            newUser(etUsername.getText().toString(),etPassword.getText().toString(),etType.getSelectedItemPosition());
            Intent resultado = new Intent();
            setResult(RESULT_OK, resultado);
            finish();
        }
        catch(Exception e)
        {
            Intent resultado = new Intent();
            setResult(RESULT_CANCELED, resultado);
            finish();
        }
    }

    private boolean newUser(String name, String password, int type)
    {
        //Insertamos datos de usuario
        int id_usuario_nuevo = dataSource.InsertUser(name,password,type);
        if(id_usuario_nuevo != -1)
        {
            int limite = 0;
            if(listProjectCheckedID!=null)
                limite = listProjectCheckedID.length;
            //Recorremos la lista de proyectos asociados al nuevo usuario.
            for(int i=0;i<limite;i++)
            {
                if(listProjectCheckedID[i]!=-0)
                {
                    dataSource.InsertUserProj(id_usuario_nuevo,listProjectCheckedID[i]);
                }
            }
            return true;
        }
        return false;
    }




}
