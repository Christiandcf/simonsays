package me.cchiang.simonsays;

import android.content.Intent;
import android.graphics.Picture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button startBtn;
    CheckBox one, three, friends;
    Random rn = new Random(System.currentTimeMillis());
    String WORDS[] = {"chair","table","person", "water", "bottle", "animal", "bird"};
    ArrayList<Integer> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This will access the random button
        addListenerOnButtonClick();


    }

    public void addListenerOnButtonClick(){


        startBtn = (Button) findViewById(R.id.startBtn);
        one =(CheckBox)findViewById(R.id.box1);
        three =(CheckBox)findViewById(R.id.box3);
        friends =(CheckBox)findViewById(R.id.boxFriends);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                StringBuilder word = new StringBuilder();
                word.append("Find: ");
//                Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                if (one.isChecked()){
                    count = 1;
                }else if(three.isChecked()){
                    count = 3;
                }
                for(int i = 0; i < count; ){
                    int x = rn.nextInt(7);
                    if(!list.contains(x)){
                        word.append(WORDS[x] + " ");
                        list.add(x);
                        i++;
                    }

                }

                setContentView(R.layout.activity_main);
                TextView randomView = (TextView)findViewById(R.id.randomView);
                randomView.setText(word + "!");



                if(friends.isChecked()){
                    Toast.makeText(MainActivity.this, "You sure you got friends...", Toast.LENGTH_SHORT).show();
                }

//                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
//                startActivity(intent);
            }
        });
    }


}
