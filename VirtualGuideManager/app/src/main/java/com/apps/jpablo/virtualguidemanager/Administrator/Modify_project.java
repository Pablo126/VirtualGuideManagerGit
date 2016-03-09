package com.apps.jpablo.virtualguidemanager.Administrator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

public class Modify_project extends ActionBarActivity {

    DBContract dataSource;
    int id_project;
    TextView tv_name, tv_description;
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
            tv_name.setText(c.getString(1));
            tv_description.setText(c.getString(2));
        }
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
        if(dataSource.UpdateProject(id_project,tv_name.getText().toString(),tv_description.getText().toString()))
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

}
