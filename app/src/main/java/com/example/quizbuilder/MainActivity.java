package com.example.quizbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText etUser;
    private Button btnStart;
    private TextView tvTopScore;
    int TopScore = 0;
    int highestScore = ScoreActivity.getHighestScore();
    static String username = "default";
    String bestPlayer = ScoreActivity.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        btnStart = findViewById(R.id.btnStart);
        tvTopScore = findViewById(R.id.tvTopScore);
        TopScore = highestScore>TopScore ? highestScore : TopScore;
        tvTopScore.setText("Highest Score: "+TopScore+"% by "+ bestPlayer);

        btnStart.setEnabled(false);

        etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                processButtonByTextLength();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                processButtonByTextLength();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                processButtonByTextLength();
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUser.getText().toString();
                etUser.setText(null);
                Intent intent = new Intent(MainActivity.this, Quiz.class);
                startActivity(intent);
            }
        });
    }

    private void processButtonByTextLength()
    {
        boolean clickable = etUser.getText().toString().trim().equals("") ? false : true;
        btnStart.setEnabled(clickable);
    }

    public static String getUsername(){
        return username;
    }

}
