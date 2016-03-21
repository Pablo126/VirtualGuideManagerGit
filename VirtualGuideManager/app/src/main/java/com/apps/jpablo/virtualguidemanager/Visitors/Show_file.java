package com.apps.jpablo.virtualguidemanager.Visitors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apps.jpablo.virtualguidemanager.R;
import com.apps.jpablo.virtualguidemanager.Storage;

import java.io.File;

public class Show_file extends Activity {

    String file_name;
    Storage st = new Storage();
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_show_file);


        im = (ImageView) findViewById(R.id.im);
        file_name = getIntent().getExtras().getString("file_name");
        String path = st.getFullPath()+file_name;

        //Abrir imagen
        File imgFile = new File(path);
        if(imgFile.exists()) {
            showControl(0,imgFile);
        }
    }


    private void showControl(int type_file, File imgFile)
    {


        switch(type_file)
        {
            case 0: {//JPG
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                im.setImageBitmap(myBitmap);
                break;
            }
            case 1: {//AUDIO
                /*MediaPlayer mc = (MediaPlayer) v;
                Uri myUri = Uri.parse(st.getFullPath()+imgFile);
                mc.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mc.setDataSource(getApplicationContext(), myUri);
                mc.prepare();
                mc.start();*/
                break;
            }
            default: {
                break;
            }
        }
    }

}
