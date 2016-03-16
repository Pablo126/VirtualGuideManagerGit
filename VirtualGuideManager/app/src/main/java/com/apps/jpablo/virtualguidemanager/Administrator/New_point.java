package com.apps.jpablo.virtualguidemanager.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.jpablo.virtualguidemanager.DBContract;
import com.apps.jpablo.virtualguidemanager.R;

import java.util.ArrayList;

public class New_point extends ActionBarActivity {


    DBContract dataSource;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_new_point);

        dataSource = new DBContract(this);
    }

    public void save(View view)
    {
        //Obtenemos los valores de los campos
        TextView tv_name = (TextView) findViewById(R.id.etInfopointName);
        TextView tv_type = (TextView) findViewById(R.id.etInfopointType);
        TextView tv_file = (TextView) findViewById(R.id.etInfopointFile);
        TextView tv_qr = (TextView) findViewById(R.id.etInfopointQR);
        //Llamamos a la función de insercción en base de datos
        if(dataSource.InsertInfopoint(tv_name.getText().toString(), Integer.parseInt(tv_type.getText().toString()), tv_file.getText().toString(), tv_qr.getText().toString()))
        {
            Intent resultado = new Intent();
            setResult(RESULT_OK, resultado);
            finish();
        }
        else
        {
            Intent resultado = new Intent();
            setResult(RESULT_CANCELED, resultado);
            finish();
        }

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

    //----------------------FILES------------------------
    public void openFolder(View view)
    {
        String folderPath = Environment.getExternalStorageDirectory()+"/VirtualGuideContent";
        Uri myUri = Uri.parse(folderPath);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(myUri, "file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //startActivityForResult(intent, 1);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);

        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //----------------------********************------------------------

    public void  onActivityResult(int requestCode, int resultCode, Intent intent){

        switch (requestCode) {
            case 0: {
                if (resultCode == RESULT_OK) {
                    String contents = intent.getStringExtra("SCAN_RESULT");
                    //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    TextView tv_qr = (TextView) findViewById(R.id.etInfopointQR);
                    tv_qr.setText(contents);
                }
                break;
            }
            case 1:
            {
                if (resultCode == RESULT_OK) {
                    String contents = intent.getData().getLastPathSegment();
                    TextView tv = (TextView) findViewById(R.id.etInfopointFile);
                    tv.setText(contents);
                }
                break;
            }
        }
    }
}
