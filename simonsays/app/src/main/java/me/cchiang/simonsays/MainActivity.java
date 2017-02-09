package me.cchiang.simonsays;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Pair;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

//FIREBASE IMPORTS
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//FIREBASE IMPORTS END


import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {

//    FIREBASE VARIABLES
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    //get current user
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    END FIREBASE VARIABLES


    private static final int READ_CONTACTS = 1000;
    private static final int WRITE_EXTERNAL_STORAGE = 1001;
    private static final int READ_EXTERNAL_STORAGE = 1002;

    public static boolean CAN_READ_CONTACTS = false;
    public static boolean CAN_WRITE_EXTERNAL_STORAGE = false;
    public static boolean CAN_READ_EXTERNAL_STORAGE = false;

    private static int count = 0;
    public static ArrayList<String> list = new ArrayList<>();
    public static StringBuilder word = new StringBuilder();



    FloatingActionButton logOutBtn;
    Button startBtn, pictureBtn;
    CheckBox one, three, friends;
    Random rn = new Random(System.currentTimeMillis());
    String WORDS[] = {"chair","table","person" , "bottle", "animal", "drink", "adult", "laptop", "telephone", "computer"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FIREBASE STUFF

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getString(R.string.app_name));
//        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();



        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

//        END FIREBASE STUFF



            count = 0;
            list = new ArrayList<>();
            word = new StringBuilder();

            generateRandom();


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.logOutBtn);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });

        // change to picture Layout
        pictureBtn = (Button) findViewById(R.id.pictureBtn);
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                startActivity(intent);
            }
        });

        checkPermissions();
    }

    private void checkPermissions() {

//        NEED TO FIX THIS CHECK HERE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, WRITE_EXTERNAL_STORAGE);

        }else {
            CAN_READ_EXTERNAL_STORAGE = true;
            CAN_WRITE_EXTERNAL_STORAGE = true;
            CAN_READ_CONTACTS = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS) {
            if(grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, PictureActivity.CAMERA_REQUEST);
                    CAN_READ_CONTACTS = true;
                }
            }
        }
        if(requestCode == WRITE_EXTERNAL_STORAGE){
            if(grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CAN_WRITE_EXTERNAL_STORAGE = true;
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, PictureActivity.CAMERA_REQUEST);
                }
            }
        }
        if (requestCode == READ_EXTERNAL_STORAGE){
            if(grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CAN_READ_EXTERNAL_STORAGE = true;
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, PictureActivity.CAMERA_REQUEST);
                }
            }
        }
    }

    public void generateRandom(){

        startBtn = (Button) findViewById(R.id.startBtn);
        one =(CheckBox)findViewById(R.id.box1);
        three =(CheckBox)findViewById(R.id.box3);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((one.isChecked() && !(three.isChecked())) || (!(one.isChecked()) && three.isChecked())){
                    list = new ArrayList<>();
                    word.append("Find: ");

                    if (one.isChecked()) {
                        count = 1;
                    } else if (three.isChecked()) {
                        count = 3;
                    }

                    for (int i = 0; i < count; ) {
                        int x = rn.nextInt(WORDS.length);
                        if (!list.contains(WORDS[x])) {
                            word.append(WORDS[x]);
                            if (i != count - 1) {
                                word.append(", ");
                            }
                            list.add(WORDS[x]);
                            i++;
                        }
                    }

                    TextView randomView = (TextView) findViewById(R.id.randomView);
                    randomView.setText(word + "!");
                    System.out.println("word is: " + word);


                    one.setEnabled(false);
                    three.setEnabled(false);
                    startBtn.setEnabled(false);
                    pictureBtn.setEnabled(true);
                }else if((one.isChecked() && three.isChecked())){
                    Toast.makeText(MainActivity.this, "Choose one only plz", Toast.LENGTH_SHORT).show();
                }else if(!one.isChecked() && !three.isChecked()){
                    Toast.makeText(MainActivity.this, "Choose one plz", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

//    FIREBASE METHODS

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

//    FIREBASE METHODS


}
