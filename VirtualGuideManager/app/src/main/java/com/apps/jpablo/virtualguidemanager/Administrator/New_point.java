package com.apps.jpablo.virtualguidemanager.Administrator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class New_point extends ActionBarActivity {


    DBContract dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_new_point);

        dataSource = new DBContract(this);
    }

    public void save(View view)
    {
        //Obtenemos los valores de los campos
        TextView tv_name = (TextView) findViewById(R.id.etInfopointName);
        TextView tv_type = (TextView) findViewById(R.id.etInfopointType);
        TextView tv_file = (TextView) findViewById(R.id.etInfopointFile);
        TextView tv_qr = (TextView) findViewById(R.id.etInfopointQR);
        //Llamamos a la función de insercción en base de datos
        if(dataSource.InsertInfopoint(tv_name.getText().toString(), Integer.parseInt(tv_type.getText().toString()), tv_file.getText().toString(), tv_qr.getText().toString()))
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
