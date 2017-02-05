//How to Capture Image from Camera and Display in Android ImageView/ Activity

package me.cchiang.simonsays;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import java.io.*;
import java.util.*;
import java.text.*;

import static android.app.Activity.RESULT_OK;

public class PictureActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView mimageView;

    Uri picUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        mimageView = (ImageView) this.findViewById(R.id.image_from_camera);
        Button button = (Button) this.findViewById(R.id.take_image_from_camera);
    }

    public void takeImageFromCamera(View view) {

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) ) {

            if(MainActivity.CAN_WRITE_EXTERNAL_STORAGE){
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        }else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            selectedImage = data.getData();
//            System.out.println(selectedImage.toString());

            photo = (Bitmap) data.getExtras().get("data");
            mimageView.setImageBitmap(photo);


        }
//        upload();
    }





}