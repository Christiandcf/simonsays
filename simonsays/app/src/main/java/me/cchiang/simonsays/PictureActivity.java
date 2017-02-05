//How to Capture Image from Camera and Display in Android ImageView/ Activity

package me.cchiang.simonsays;

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

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

//            File file=getOutputMediaFile(1);
//            picUri = Uri.fromFile(file);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }

    }


    public String makeBitmapPath(Bitmap bmp){
        // TimeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path, "Photo"+ timeStamp +".jpg"); // the File to save to
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        } catch (IOException e){
            // whatever
        }
        return file.getPath();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

//            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
//            mimageView.setImageBitmap(mphoto);

            selectedImage = data.getData();
//            selectedImage = data.getExtras();

            photo = (Bitmap) data.getExtras().get("data");
            mimageView.setImageBitmap(photo);

            String path = makeBitmapPath(photo);
            selectedImage = Uri.parse(path);

            System.out.println("path is " + path);


//            photo = (Bitmap) data.getExtras().get("data");

            // Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
//
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();


        }
//        upload();
    }



    private void upload() {
        // Image location URL
        Log.e("path", "----------------" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        //ba1 = Base64.encodeBytes(ba);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new PictureActivity.uploadToServer().execute();

    }




    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(PictureActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        private String readStream(InputStream is) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = "";
            String ba1 = "";
            ArrayList<Pair<String, String>> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new Pair<>("base64", ba1));
            nameValuePairs.add(new Pair<>("ImageName", System.currentTimeMillis() + ".jpg"));
            try {
                URL url = new URL("http://proj-309-gb-3.cs.iastate.edu/php/uploadImage.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result = readStream(in);
                    System.out.println("result is : " + result);
                } finally {
                    urlConnection.disconnect();
                }


//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
//                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
}