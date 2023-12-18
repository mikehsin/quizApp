package com.example.quizbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView tvfinalScore;
    private Button btnAgain;
    int score = Quiz.getScore();
    private static int highestScore = 0;
    String thisplayer = MainActivity.getUsername();
    private static String username = "default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tvfinalScore = findViewById(R.id.tvFinalScore);
        btnAgain =findViewById(R.id.btnAgain);
        int total = 20;
        final int finalScore = (int) Math.round(score / (double) total *100);
        int color = finalScore > 60 ? Color.GREEN : Color.RED;
        tvfinalScore.setTextColor(color);
        tvfinalScore.setText(finalScore+"%");

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = finalScore > highestScore ? thisplayer : username;
                highestScore =  finalScore >highestScore ? finalScore : highestScore;
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public static int getHighestScore(){
        return highestScore;
    }

    public static String getUsername(){
        return username;
    }

}
