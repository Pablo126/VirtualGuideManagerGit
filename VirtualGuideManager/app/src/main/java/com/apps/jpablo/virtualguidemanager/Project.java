package com.apps.jpablo.virtualguidemanager;

/**
 * Created by JuanPablo on 15/03/2016.
 */
//Clase project para crear una estructura comoda para gestionar los proyectos checked
public class Project {
    private int projectID;
    private String projectName;
    private boolean projectSelected;

    public Project(int a,String b ,boolean c)
    {
        projectID = a;
        projectName= b;
        projectSelected = c;
    }

    public int getID(){return projectID;}
    public String getName(){return projectName;}
    public boolean getSelected(){return projectSelected;}
    public void setSelected(boolean selected){projectSelected = selected;}

}
