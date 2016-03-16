package com.apps.jpablo.virtualguidemanager;

import android.os.Environment;

/**
 * Created by JuanPablo on 16/03/2016.
 */
public class Storage {

    private String path_folder;
    private boolean external;
    private DBContract dataSource=null;
    public Storage(String path,boolean external_on)
    {
        path_folder = path;
        external = external_on;
    }

    //Crea un objeto de base de datos, consulta los valores y los establece automaticamente.
    public Storage()
    {

    }

    public void setPath(String path)
    {
        path_folder = path;
        setPathDatabase(path);
    }

    //Realiza la actualización de campo en la base de datos
    private void setPathDatabase(String path)
    {

    }



    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
