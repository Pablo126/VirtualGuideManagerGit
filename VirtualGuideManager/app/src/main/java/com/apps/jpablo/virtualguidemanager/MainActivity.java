package com.apps.jpablo.virtualguidemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apps.jpablo.virtualguidemanager.Administrator.Main;


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
            if(user_type== 0)
            {
                Intent intent = new Intent(this, com.apps.jpablo.virtualguidemanager.Administrator.Main.class);
                intent.putExtra("id_usuario",id_user);
                startActivity(intent);
            }
            else if(user_type== 1)
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
