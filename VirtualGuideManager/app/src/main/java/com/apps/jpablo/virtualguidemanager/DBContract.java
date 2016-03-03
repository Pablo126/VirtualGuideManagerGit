package com.apps.jpablo.virtualguidemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by JuanPablo on 01/03/2016.
 * Ref:
 * http://www.hermosaprogramacion.com/2014/10/android-sqlite-bases-de-datos/
 * http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DBContract {

        //Metainformación de la base de datos
        public static final String USER_TABLE_NAME = "Users";
        public static final String PROJECTS_TABLE_NAME = "Projects";
        public static final String USER_PROJ_TABLE_NAME = "User_proj";
        public static final String STRING_TYPE = "text";
        public static final String INT_TYPE = "integer";

        //Campos de la tabla Users
        public static class ColumnUsers{
            public static final String ID = BaseColumns._ID;
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String ID_TYPE = "id_type";
        }

        //Campos de la tabla Projects
        public static class ColumnProjects{
            public static final String ID = BaseColumns._ID;
            public static final String NAME = "name";
            public static final String ID_TYPE = "id_type";
            public static final String BACKGROUND = "img_background";
        }

        //Campos de la tabla User_proj
        public static class ColumnUser_proj{
            public static final String ID_USER = "id_user";
            public static final String ID_PROJECT = "id_project";
        }
        //Script de Creación de la tabla Users
        public static final String CREATE_USERS_SCRIPT =
                "create table "+USER_TABLE_NAME+"(" +
                        ColumnUsers.ID+" "+INT_TYPE+" primary key autoincrement," +
                        ColumnUsers.USERNAME+" "+STRING_TYPE+" not null," +
                        ColumnUsers.PASSWORD+" "+STRING_TYPE+" not null," +
                        ColumnUsers.ID_TYPE+" "+INT_TYPE+" not null)";

        //Script de Creación de la tabla Projects
        public static final String CREATE_PROJECTS_SCRIPT =
                "create table "+PROJECTS_TABLE_NAME+"(" +
                        ColumnProjects.ID+" "+INT_TYPE+" primary key autoincrement," +
                        ColumnProjects.NAME+" "+STRING_TYPE+" not null," +
                        ColumnProjects.ID_TYPE+" "+INT_TYPE+" not null," +
                        ColumnProjects.BACKGROUND+" "+STRING_TYPE+")";

        //Script de Creación de la tabla User_proj
        public static final String CREATE_USERS_PROJ_SCRIPT =
                "create table "+USER_PROJ_TABLE_NAME+"(" +
                        ColumnUser_proj.ID_USER+" "+INT_TYPE+" not null ," +
                        ColumnUser_proj.ID_PROJECT+" "+INT_TYPE+" not null," +
                        "PRIMARY KEY ("+ColumnUser_proj.ID_USER+", "+ColumnUser_proj.ID_PROJECT+"))";

        //Scripts de inserción por defecto
        public static final String INSERT_USERS_SCRIPT =
                "insert into "+USER_TABLE_NAME+" values(" +
                        "null," + "\"Admin\"," + "\"1234\"," + "\"0\")," +
                        "(null," + "\"User1\"," + "\"1234\"," + "\"1\")";

        //Scripts de inserción por defecto
        public static final String INSERT_PROJECTS_SCRIPT =
                "insert into "+PROJECTS_TABLE_NAME+" values(" +
                        "null," + "\"Proj1\"," + "\"0\"," + "\"\")," +
                        "(null," + "\"Proj2\"," + "\"0\"," + "\"\")";

        //Scripts de inserción por defecto
        public static final String INSERT_USER_PROJ_SCRIPT =
                "insert into "+USER_PROJ_TABLE_NAME+" values(" +
                        "0," + "\"0\")," +
                        "(0," + "\"1\")," +
                        "(1," + "\"0\")";

        private DBHelper openHelper;
        private SQLiteDatabase database;

        public DBContract(Context context) {
            //Creando una instancia hacia la base de datos
            openHelper = new DBHelper(context);
            database = openHelper.getWritableDatabase();
        }

        public Cursor Select(String query)
        {
            return database.rawQuery(query, null);
        }

}
