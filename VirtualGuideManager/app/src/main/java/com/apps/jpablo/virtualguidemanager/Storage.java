package com.apps.jpablo.virtualguidemanager;

import android.os.Environment;

/**
 * Created by JuanPablo on 16/03/2016.
 */
public class Storage {

    private String path_folder = "/storage/sdcard1/";
    private String name_folder = "VirtualGuideContent/";
    private String path;
    private DBContract dataSource=null;
    public Storage()
    {
        path = path_folder+name_folder;
    }

    public String getFullPath()
    {
        return path;
    }


    public void setPathFolder(String path)
    {
        path_folder = path;
        setPathDatabase(path);
    }

    public void setNameFolder(String name)
    {
        name_folder = name;
        setNameFolderDatabase(name);
    }

    //Realiza la actualización de campo en la base de datos
    private void setPathDatabase(String path)
    {

    }

    //Realiza la actualización de campo en la base de datos
    private void setNameFolderDatabase(String name_folder)
    {

    }


}
