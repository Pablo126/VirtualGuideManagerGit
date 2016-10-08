package com.apps.jpablo.virtualguidemanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.Classes.DBContract;
import com.apps.jpablo.virtualguidemanager.Classes.User_type;


public class MainActivity extends ActionBarActivity {


    DBContract dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Crear nuevo objeto QuotesDataSource
        dataSource = new DBContract(this);
    }

    public void login(View view) {
        //Extraemos los campos de login
        TextView tv_username = (TextView) findViewById(R.id.tvUsername);
        TextView tv_password = (TextView) findViewById(R.id.tvPassword);
        //Si existe el usuario
        Cursor c1 = dataSource.Select("Select * from Users where username='"+tv_username.getText()+"' and password='"+tv_password.getText()+"'",null);
        if(c1.moveToNext())
        {
            int user_type = c1.getInt(3);
            int id_user = c1.getInt(0);
            //Obtenemos el tipo de usuario que es
            if(user_type== User_type.ADMIN.getValue())
            {
                Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.Main.class);
                intent.putExtra("id_usuario",id_user);
                startActivity(intent);
            }
            else if(user_type== User_type.USER.getValue())
            {
                Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Visitors.Main.class);
                intent.putExtra("id_usuario",id_user);
                startActivity(intent);
            }
        }
        else
        {

        }

    }



}
