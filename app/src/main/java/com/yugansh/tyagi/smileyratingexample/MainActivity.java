package com.yugansh.tyagi.smileyratingexample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import com.yugansh.tyagi.smileyrating.SmileyRatingView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    SmileyRatingView smileyRating;
    Integer smileyID = 0;
    boolean mouthAnimation = true;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smileyRating = findViewById(R.id.smiley_view);

        smileyRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Integer id = GetNewSmiley();
              //  smileyRating.setSmiley(id);

                timer();
            }
        });
    }

    private void timer() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smileyRating.ChangeMouth(15, 5);
                handler.postDelayed(this, new Random().nextInt(30 - 15 + 1)+ 15);
            }
        }, 10);
    }


    private int GetNewSmiley(){
        if(smileyID < 4)
            smileyID++;
        else
            smileyID = 0;

        return smileyID;
    }
}
