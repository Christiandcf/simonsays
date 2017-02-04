package com.example.zac.testing_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sample_activity extends AppCompatActivity {

    Button button1;
    Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_activity);

        button1 = (Button) findViewById(R.id.screen_1_button);
        button3 = (Button) findViewById(R.id.screen_3_button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sample_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sample_activity.this, screen_3_activity.class);
                startActivity(intent);
            }
        });
    }
}
