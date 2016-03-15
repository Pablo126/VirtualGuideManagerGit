package com.apps.jpablo.virtualguidemanager;

/**
 * Created by JuanPablo on 15/03/2016.
 */
//Clase project para crear una estructura comoda para gestionar los infopoints checked
public class Infopoint {
    private int infopointID;
    private String infopointName;
    private boolean infopointSelected;

    public Infopoint(int a,String b ,boolean c)
    {
        infopointID = a;
        infopointName= b;
        infopointSelected = c;
    }

    public int getID(){return infopointID;}
    public String getName(){return infopointName;}
    public boolean getSelected(){return infopointSelected;}
    public void setSelected(boolean selected){infopointSelected = selected;}

}

