package com.apps.jpablo.virtualguidemanager.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

public class New_project extends ActionBarActivity {

    DBContract dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_new_project);

        dataSource = new DBContract(this);
    }


    public void save(View view)
    {
        //Obtenemos los valores de los campos
        TextView tv_name = (TextView) findViewById(R.id.etName);
        TextView tv_description = (TextView) findViewById(R.id.etDescription);
        //Llamamos a la función de insercción en base de datos
        if(dataSource.InsertProject(tv_name.getText().toString(), tv_description.getText().toString()))
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
