package com.apps.jpablo.virtualguidemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
        public static final String INFO_POINT_TABLENAME = "Infopoints";
        public static final String IP_PROJ_TABLE_NAME = "Ip_proj";
        public static final String FILE_TYPE_TABLE_NAME = "File_type";
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
            public static final String DESCRIPTION = "description";
            public static final String ID_TYPE = "id_type";
            public static final String BACKGROUND = "img_background";
        }

        //Campos de la tabla User_proj
        public static class ColumnUser_proj{
            public static final String ID_USER = "id_user";
            public static final String ID_PROJECT = "id_project";
        }

        //Campos de la tabla Infopoint
        public static class ColumnInfopoint{
            public static final String ID = BaseColumns._ID;
            public static final String NAME = "name";
            public static final String ID_TYPE = "id_type";
            public static final String FILE = "file";
            public static final String QR = "qr";
        }

        //Campos de la tabla Ip_proj
        public static class ColumnIn_proj{
            public static final String ID_INFOPOINT = "id_ip";
            public static final String ID_PROJECT = "id_project";
        }

        //Campos de la tabla File_type
        public static class columnFile_type{
            public static final String ID = BaseColumns._ID;
            public static final String DESCRIPTION = "description";
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
                        ColumnProjects.DESCRIPTION+" "+STRING_TYPE+" not null," +
                        ColumnProjects.ID_TYPE+" "+INT_TYPE+" not null," +
                        ColumnProjects.BACKGROUND+" "+STRING_TYPE+")";

        //Script de Creación de la tabla User_proj
        public static final String CREATE_USERS_PROJ_SCRIPT =
                "create table "+USER_PROJ_TABLE_NAME+"(" +
                        ColumnUser_proj.ID_USER+" "+INT_TYPE+" not null ," +
                        ColumnUser_proj.ID_PROJECT+" "+INT_TYPE+" not null," +
                        "PRIMARY KEY ("+ColumnUser_proj.ID_USER+", "+ColumnUser_proj.ID_PROJECT+"))";

        //Script de Creación de la tabla INFOPOINTS
        public static final String CREATE_INFOPOINTS_SCRIPT =
                "create table "+INFO_POINT_TABLENAME+"(" +
                        ColumnInfopoint.ID+" "+INT_TYPE+" primary key autoincrement," +
                        ColumnInfopoint.NAME+" "+STRING_TYPE+" not null," +
                        ColumnInfopoint.ID_TYPE+" "+INT_TYPE+"," +
                        ColumnInfopoint.FILE+" "+STRING_TYPE+"," +
                        ColumnInfopoint.QR+" "+STRING_TYPE+")";

        //Script de Creación de la tabla IP_PROJ
        public static final String CREATE_IP_PROJ_SCRIPT =
                "create table "+IP_PROJ_TABLE_NAME+"(" +
                        ColumnIn_proj.ID_INFOPOINT+" "+INT_TYPE+" not null ," +
                        ColumnIn_proj.ID_PROJECT+" "+INT_TYPE+" not null," +
                        "PRIMARY KEY ("+ColumnIn_proj.ID_INFOPOINT+", "+ColumnIn_proj.ID_PROJECT+"))";

        //Script de Creación de la tabla File_type
        public static final String CREATE_FILE_TYPE_SCRIPT =
                "create table "+FILE_TYPE_TABLE_NAME+"(" +
                        columnFile_type.ID+" "+INT_TYPE+" primary key autoincrement," +
                        columnFile_type.DESCRIPTION+" "+STRING_TYPE+")";


        //Scripts de inserción por defecto
        public static final String INSERT_USERS_SCRIPT =
                "insert into "+USER_TABLE_NAME+" values(" +
                        "null," + "\"Admin\"," + "\"1234\"," + "\"0\")," +
                        "(null," + "\"User1\"," + "\"1234\"," + "\"1\")";

        //Scripts de inserción por defecto
        public static final String INSERT_PROJECTS_SCRIPT =
                "insert into "+PROJECTS_TABLE_NAME+" values(" +
                        "null," + "\"Proj1\"," + "\"Descripción proyecto 1\"," + "\"0\"," + "\"\")," +
                        "(null," + "\"Proj2\"," + "\"Esta es la descripcion del proyecto 2\"," + "\"0\"," + "\"\")";

        //Scripts de inserción por defecto
        public static final String INSERT_USER_PROJ_SCRIPT =
                "insert into "+USER_PROJ_TABLE_NAME+" values(" +
                        "1," + "\"1\")," +
                        "(1," + "\"2\")," +
                        "(2," + "\"1\")";

        //Scripts de inserción por defecto
        public static final String INSERT_INFOPOINTS_SCRIPT =
                "insert into "+INFO_POINT_TABLENAME+" values(" +
                        "null," + "\"Punto1\"," + "\"0\"," + "\"NINGUNO\"," + "\"qr1\")," +
                        "(null," + "\"Punto2\"," + "\"0\"," + "\"\"," + "\"qr2\")";

        //Scripts de inserción por defecto
        public static final String INSERT_IN_PROJ_SCRIPT =
                "insert into "+IP_PROJ_TABLE_NAME+" values(" +
                        "1," + "\"1\")," +
                        "(2," + "\"1\")," +
                        "(1," + "\"2\")";

        private DBHelper openHelper;
        private SQLiteDatabase database;

        public DBContract(Context context) {
            //Creando una instancia hacia la base de datos
            openHelper = new DBHelper(context);
            database = openHelper.getWritableDatabase();
        }

        public Cursor Select(String query, String[] selectionArgs)
        {
            return database.rawQuery(query, selectionArgs);
        }

        public boolean InsertProject(String name,String description){
            try {
                String insert = "INSERT INTO " + PROJECTS_TABLE_NAME + " ("
                        + ColumnProjects.NAME + ", "
                        + ColumnProjects.DESCRIPTION + ", " + ColumnProjects.ID_TYPE + ", "
                        + ColumnProjects.BACKGROUND + ") Values ('"+name+"', '"+description+"','' , '')";
                database.execSQL(insert);
                Cursor c = database.rawQuery("SELECT last_insert_rowid()",null);
                c.moveToFirst();
                int id = c.getInt(0);
                String insert2 = "INSERT INTO " + USER_PROJ_TABLE_NAME + " ("
                        + ColumnUser_proj.ID_USER + ", "
                        + ColumnUser_proj.ID_PROJECT + ") Values ('1', '"+id+"')";
                database.execSQL(insert2);
                return true;
            }
            catch(SQLException e)
            {
                return false;
            }
        }

        public boolean Delete(String table, String column_key, String[] where_values)
        {
            try {
                database.delete(table, column_key + " = ?", where_values);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        public boolean UpdateProject(int id_project, String name, String description)
        {
            try {
                ContentValues values = new ContentValues();
                values.put(ColumnProjects.NAME, name);
                values.put(ColumnProjects.DESCRIPTION, description);

                String selection = ColumnProjects.ID + " = ?";
                String[] selectionArgs = {String.valueOf(id_project)};
                database.update(PROJECTS_TABLE_NAME, values, selection, selectionArgs);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

}
