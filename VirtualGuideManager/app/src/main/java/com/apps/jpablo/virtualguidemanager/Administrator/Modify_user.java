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
import com.apps.jpablo.virtualguidemanager.Classes.Project;
import com.apps.jpablo.virtualguidemanager.Classes.User_type;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modify_user extends ActionBarActivity {


    DBContract dataSource;
    Cursor c1 = null;
    String[] listProjectsString;
    int[] listProjectCheckedID;
    boolean[] listProjectChecked;
    //ArrayList with names of projects
    ArrayList<Project> listProjects = new ArrayList<Project>();
    int id_usuario;
    TextView tv_username, tv_password;
    Spinner tv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_modify_user);

        //Recuperamos el id de usuario
        id_usuario = getIntent().getExtras().getInt("id_usuario_mod");

        dataSource = new DBContract(this);

        Spinner spinner = (Spinner) findViewById(R.id.tvUserType);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item);
        List<User_type> Users = Arrays.asList(User_type.values());
        List<String> combo = new ArrayList<String>();
        for(User_type u : Users){
            combo.add(u.getName(this.getResources()));}
        adapter.addAll(combo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Hacemos una consulta a database para cargar los datos
        Cursor c = dataSource.Select("Select * from "+DBContract.USER_TABLE_NAME+" where "+DBContract.ColumnUsers.ID+"="+id_usuario,null);
        if(c.moveToFirst())
        {
            tv_username = (TextView) findViewById(R.id.etUsername);
            tv_password = (TextView) findViewById(R.id.etUserPassword);
            tv_type = (Spinner) findViewById(R.id.tvUserType);
            tv_username.setText(c.getString(1));
            tv_password.setText(c.getString(2));
            tv_type.setSelection(c.getInt(3));
        }

        loadAllProjects();
        loadProjectsLinked();

    }

    //Función para carga la lista ed proyectos
    public void loadAllProjects() {
        //Query todos los proyectos
        String query = "Select "+ DBContract.ColumnProjects.ID+", "+DBContract.ColumnProjects.NAME+" from "+ DBContract.PROJECTS_TABLE_NAME;
        c1 = dataSource.Select(query, null);
        //Adding results of query to arraylist
        if(c1.moveToFirst()) {
            do {
                listProjects.add(new Project(c1.getInt(0), c1.getString(1), false));
            }
            while (c1.moveToNext());

            String[] ArrayStringProj = new String[listProjects.size()];
            ArrayList<String> aux = new ArrayList<String>();
            for (Project p : listProjects)
                aux.add(p.getName());
            aux.toArray(ArrayStringProj);
            listProjectsString = ArrayStringProj;
        }
    }

    private void loadProjectsLinked()
    {
        Cursor c2 = dataSource.Select("Select * from "+DBContract.USER_PROJ_TABLE_NAME+" where "+DBContract.ColumnUser_proj.ID_USER+"="+id_usuario,null);
        if(c2.moveToFirst()) {
            do {
                for (Project p : listProjects) {

                    if (p.getID() == c2.getInt(1))
                        p.setSelected(true);
                }
            }
            while (c2.moveToNext());
            refreshListProjectSelected();
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

    public void SaveUser(View view)
    {
        try
        {
            EditText etUsername = (EditText) findViewById(R.id.etUsername);
            EditText etPassword = (EditText) findViewById(R.id.etUserPassword);
            Spinner etType = (Spinner) findViewById(R.id.tvUserType);
            if(modifyUser(etUsername.getText().toString(), etPassword.getText().toString(), etType.getSelectedItemPosition()))
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
        catch(Exception e)
        {
            finishActivity(RESULT_CANCELED);
        }
    }

    private boolean modifyUser(String name, String password, int type)
    {
        //Insertamos datos de usuario

        if(dataSource.UpdateUser(id_usuario, name,password,type))
        {
            //Eliminamos los proyectos anteriormente enlazados a este usuario
            String[] values = {String.valueOf(id_usuario)};
            if(dataSource.Delete(DBContract.USER_PROJ_TABLE_NAME, DBContract.ColumnUser_proj.ID_USER, values))
            {
                int limite = 0;
                if(listProjectCheckedID!=null)
                    limite = listProjectCheckedID.length;
                //Recorremos la lista de proyectos asociados al nuevo usuario.
                for (int i = 0; i < limite; i++) {
                    if (listProjectCheckedID[i] != -0) {
                        dataSource.InsertUserProj(id_usuario, listProjectCheckedID[i]);
                    }
                }
            }
            return true;
        }
        return false;
    }





    public void showProjectList(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoSeleccion dialogo = new DialogoSeleccion(listProjectsString);
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

}
