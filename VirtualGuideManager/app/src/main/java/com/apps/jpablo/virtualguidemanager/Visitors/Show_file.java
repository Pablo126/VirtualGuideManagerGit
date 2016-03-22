package com.apps.jpablo.virtualguidemanager.Visitors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.apps.jpablo.virtualguidemanager.R;
import com.apps.jpablo.virtualguidemanager.Storage;

import java.io.File;

public class Show_file extends Activity {

    String file_name;
    Storage st = new Storage();
    ImageView im;
    VideoView vv;
    MediaController mc;
    TextView tv;

    private MediaController mediaControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_show_file);

        file_name = getIntent().getExtras().getString("file_name");
        String path = st.getFullPath() + file_name;
        im = (ImageView) findViewById(R.id.imageView);
        vv = (VideoView) findViewById(R.id.videoView);
        tv = (TextView) findViewById(R.id.textView2);
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.start();
                mc.show(0);
            }
        });
        //Abrir imagen
        File imgFile = new File(path);
        if(imgFile.exists()) {
            showFile(typeFile(file_name), imgFile);
        }




    }



    private void showFile(int type_file, File imgFile)
    {


        switch(type_file)
        {
            case 1: {//JPG
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                im.setImageBitmap(myBitmap);
                vv.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
                break;
            }
            case 2: {//VIDEO
                mc = new MediaController(this);
                mc.setAnchorView(vv);
                mc.setMediaPlayer(vv);
                Uri video = Uri.parse(imgFile.getAbsolutePath());
                vv.setMediaController(mc);
                vv.setVideoURI(video);
                vv.start();
                im.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
                break;
            }
            case 3: { //AUDIO
                mc = new MediaController(this);
                mc.setAnchorView(vv);
                mc.setMediaPlayer(vv);
                Uri video = Uri.parse(imgFile.getAbsolutePath());
                vv.setMediaController(mc);
                vv.setVideoURI(video);
                tv.setText(file_name);
                im.setVisibility(View.INVISIBLE);
                vv.setBackgroundColor(Color.WHITE);
                vv.setBackgroundColor(Color.TRANSPARENT);
                break;
            }
            case 4: {//PDF
                    Uri path = Uri.fromFile(imgFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {startActivity(intent);
                        finish();
                    }
                    catch (ActivityNotFoundException e) {
                        Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();}
                break;
            }
            default: {
                Toast.makeText(this, "The file can't be opened or the file type is not support.or the file type not is compatible.", Toast.LENGTH_LONG).show();
                finish();
                break;
            }
        }
    }

    private static final int IMAGE = 1;
    private static final int VIDEO = 2;
    private static final int AUDIO = 3;
    private static final int PDF = 4;
    private static final int UNKNOWN = 0;

    private int typeFile(String file_name)
    {
        String extension = file_name.substring(file_name.lastIndexOf(".") + 1, file_name.length());
        extension = extension.toLowerCase();
        int value=0;
        switch(extension)
        {
            case "jpg":
                value = IMAGE; break;
            case "png":
                value = IMAGE; break;
            case "mp4":
                value = VIDEO; break;
            case "mp3":
                value = AUDIO; break;
            case "pdf":
                value = PDF; break;
        }
        return value;
    }





}
