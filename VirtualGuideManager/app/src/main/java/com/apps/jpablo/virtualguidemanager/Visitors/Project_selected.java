package com.apps.jpablo.virtualguidemanager.Visitors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;
import com.apps.jpablo.virtualguidemanager.Storage;

import java.io.File;

public class Project_selected extends Activity {

    DBContract dataSource;
    int id_project;
    String background;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private static final int SHOW_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_project_selected);

        dataSource = new DBContract(this);
        //Recuperamos el id de usuario
        id_project = getIntent().getExtras().getInt("id_project");

        //Hacemos una consulta a database para cargar los datos
        Cursor c = dataSource.Select("Select * from "+DBContract.PROJECTS_TABLE_NAME+" where "+DBContract.ColumnProjects.ID+"="+id_project,null);
        if(c.moveToFirst())
        {
            background = c.getString(4);

        }
        Storage st = new Storage();
        String path = st.getFullPath()+background;
        File imgFile = new File(path);
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Resources res = getResources();
            BitmapDrawable bd = new BitmapDrawable(res, myBitmap);
            View view = findViewById(R.id.container);
            view.setBackground(bd);
        }
        else
            path = "0";
            //Toast.makeText(this.getContext(), "no IMAGE IS PRESENT'", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }


    //----------------------SCAN QR CODE------------------------
    //product qr code mode
    public void scanQR(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }
    //----------------------********************------------------------
    public void  onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case 0: {
                if (resultCode == RESULT_OK) {
                    String contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    Intent intent2 = new Intent(this, com.apps.jpablo.virtualguidemanager.Visitors.Show_file.class);
                    String file_name = getFilePathFromInfopoint(contents);
                    intent2.putExtra("file_name",file_name);
                    startActivityForResult(intent2, SHOW_FILE, null);
                }
                break;
            }
        }
    }

    //--------------------------------------------------------------
    private String getFilePathFromInfopoint(String infopoint_value)
    {
        String query = "Select "+DBContract.ColumnInfopoint.FILE+" from "+DBContract.INFO_POINT_TABLENAME+" where "+DBContract.ColumnInfopoint.QR+" = '"+infopoint_value+"'";
        Cursor c1 = dataSource.Select(query,null);
        if(c1.moveToFirst())
        {
            return c1.getString(0);
        }
        return "";
    }




}
