package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by JuanPablo on 15/03/2016.
 */
public class Menu_adm extends Activity {
    private Activity instance = null;
    private int id_user;
    Menu_adm(Activity class_ref, int id_usuario)
    {
        instance = class_ref;
        id_user = id_usuario;
    }

    public Intent gotoProjectManager()
    {
        Intent intent = new Intent(instance, com.apps.jpablo.virtualguidemanager.Administrator.Main.class);
        intent.putExtra("id_usuario", id_user);
        return intent;
    }

    public Intent gotoUserManager()
    {
        Intent intent = new Intent(instance, com.apps.jpablo.virtualguidemanager.Administrator.Main_users.class);
        intent.putExtra("id_usuario",id_user);
        return intent;
    }

    public Intent gotoInfopointsManager()
    {
        Intent intent = new Intent(instance, com.apps.jpablo.virtualguidemanager.Administrator.Main_infopoints.class);
        intent.putExtra("id_usuario",id_user);
        return intent;
    }
}
