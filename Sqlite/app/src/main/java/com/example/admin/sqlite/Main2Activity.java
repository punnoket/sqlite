package com.example.admin.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    private String filePath;
    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

                final ImageButton btn1 = (ImageButton) findViewById(R.id.button1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(new Date());
                        String imageFileName = timeStamp + ".jpg";
                        File f = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES),imageFileName);
                        Uri fileUri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                filePath = fileUri.toString();
                fileName = f.getName();
                startActivityForResult(intent, 0);
            }
        });

        final ImageButton btn2 = (ImageButton) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 6);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent, 1);
                }
            }
        });

        final ImageButton btn3 = (ImageButton)
                findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        final ImageButton btn4 = (ImageButton)
                findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bmpPic = BitmapFactory.decodeFile(
                        filePath.replace("file://", ""));
                FileOutputStream bmpFile = new FileOutputStream(
                        filePath.replace("file://", ""));
                bmpPic = Bitmap.createScaledBitmap(bmpPic, 600, 400, true);
                Matrix mat = new Matrix();
                mat.postRotate(90);
                bmpPic = Bitmap.createBitmap(bmpPic, 0, 0,
                        bmpPic.getWidth(), bmpPic.getHeight(), mat, true);
                bmpPic.compress(Bitmap.CompressFormat.JPEG, 50, bmpFile);
                bmpFile.flush();
                bmpFile.close();
                ImageButton img = (ImageButton) findViewById(R.id.button3);
                img.setImageBitmap(bmpPic);
                TextView txt = (TextView) findViewById(R.id.textview);
                txt.setText("Camera");
            } catch (Exception e) {
                Log.e("Log", "Error from Camera Activity");
            }
        }
        if (requestCode== 1 && resultCode == Activity.RESULT_OK && data!=null) {
            try {
                ImageButton img = (ImageButton) findViewById(R.id.button2);
                img.setImageResource(android.R.drawable.ic_media_play);
                TextView txt = (TextView) findViewById(R.id.textview);
                txt.setText("Video");
            } catch (Exception e) {
                Log.e("Log", "Error from Video Activity");
            }
        }

        if (requestCode== 2 && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bmpPic = BitmapFactory.decodeFile(imgDecodableString);
                ImageButton img = (ImageButton) findViewById(R.id.button3);
                img.setImageBitmap(bmpPic);
                TextView txt = (TextView) findViewById(R.id.textview);
                txt.setText("Gallery");
            } catch (Exception e) {
                Log.e("Log", "Error from Gallery Activity");
            }
        }

        if (requestCode== 3 && resultCode == Activity.RESULT_OK) {
            try {

                TextView txt = (TextView) findViewById(R.id.textview);
                txt.setText("Audio");
            } catch (Exception e) {
                Log.e("Log", "Error from Gallery Activity");
            }
        }

    }


}
