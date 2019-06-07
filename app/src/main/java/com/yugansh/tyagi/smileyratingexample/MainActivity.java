package com.yugansh.tyagi.smileyratingexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import com.yugansh.tyagi.smileyrating.SmileyRatingView;

public class MainActivity extends AppCompatActivity {
    SmileyRatingView smileyRating;
    Integer smileyID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smileyRating = findViewById(R.id.smiley_view);

        smileyRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Integer id = GetNewSmiley();
                smileyRating.setSmiley(id);
            }
        });
    }

    private int GetNewSmiley(){
        if(smileyID < 4)
            smileyID++;
        else
            smileyID = 0;

        return smileyID;
    }
}
