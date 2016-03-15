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

public class Modify_point extends ActionBarActivity {

    DBContract dataSource;
    int id_infopoint;
    TextView tv_name, tv_type, tv_file, tv_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_modify_point);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);
        //Recuperamos el id de usuario
        id_infopoint = getIntent().getExtras().getInt("id_punto_mod");

        //Hacemos una consulta a database para cargar los datos
        Cursor c = dataSource.Select("Select * from "+DBContract.INFO_POINT_TABLENAME+" where "+DBContract.ColumnInfopoint.ID+"="+id_infopoint,null);
        if(c.moveToFirst())
        {
            tv_name = (TextView) findViewById(R.id.etInfopointName);
            tv_type = (TextView) findViewById(R.id.etInfopointType);
            tv_file = (TextView) findViewById(R.id.etInfopointFile);
            tv_qr = (TextView) findViewById(R.id.etInfopointQR);
            tv_name.setText(c.getString(1));
            tv_type.setText(String.valueOf(c.getInt(2)));
            tv_file.setText(c.getString(3));
            tv_qr.setText(c.getString(4));
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

    public void updateInfopoint(View view)
    {
        if(dataSource.UpdateInfopoint(id_infopoint, tv_name.getText().toString(), Integer.parseInt(tv_type.getText().toString()), tv_file.getText().toString(), tv_qr.getText().toString()))
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
