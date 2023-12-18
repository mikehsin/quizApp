package com.example.quizbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class Quiz extends AppCompatActivity {

    private TextView tvCOuntDown, tvScore, tvQuestionNum, tvQuestion;
    private RadioButton option1, option2, option3, option4, radioButton;
    private Button btnNext;
    private RadioGroup radioGroup;
    private static int score = 0;
    boolean isCounterRunning  = false;
    int num = 0;
    String selection = "";
    List<Integer> objects = new ArrayList<Integer>();
    List<Button> buttons = new ArrayList<Button>();
    HashMap<String, String> map = new HashMap<String, String>();

    CountDownTimer Timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int color = millisUntilFinished >= 10000 ? Color.GREEN : Color.RED;
            tvCOuntDown.setTextColor(color);
            String msg = millisUntilFinished >= 10000 ? "00: "+millisUntilFinished/1000 : "00: 0"+millisUntilFinished/1000;
            tvCOuntDown.setText(msg);
        }

        @Override
        public void onFinish() {
            tvCOuntDown.setTextColor(Color.RED);
            tvCOuntDown.setText("Times Up");
            isCounterRunning = false;

            if (radioGroup.getCheckedRadioButtonId() == -1) {
                radioGroup.setClickable(false);
                btnNext.setEnabled(true);
                btnNext.performClick();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvCOuntDown = findViewById(R.id.tvCountDown);
        tvScore = findViewById(R.id.tvScore);
        tvQuestionNum = findViewById(R.id.tvQuestionNum);
        tvQuestion = findViewById(R.id.tvQuestion);
        radioGroup = findViewById(R.id.RadioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        btnNext = findViewById(R.id.btnNext);
        tvQuestion.setMovementMethod(new ScrollingMovementMethod());
        objects.add(0); objects.add(1); objects.add(2); objects.add(3);
        buttons.add(option1); buttons.add(option2); buttons.add(option3); buttons.add(option4);
        String line = "";
        InputStream file = this.getResources().openRawResource(R.raw.quiz);
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));

        if (file != null) {
            try {
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":", 2);
                    if (parts.length >= 2)
                    {
                        String key = parts[0];
                        String value = parts[1];
                        map.put(key, value);
                    } else {
                        System.out.println("Syntax Error: File read permission");
                    }
                }
                file.close();
            }
            catch (Exception e) {
                Log.e("Error", "Error Occur while reading file", e);
                e.printStackTrace();
            }
        } //end read/write file

        final ArrayList<String>definition = new ArrayList<>(map.keySet());
        ArrayList<String>temp = new ArrayList<String>(map.keySet());
        temp.remove(num);
        System.out.println(temp);
        Collections.shuffle(definition);
        refreshPage(definition);
        btnNext.setEnabled(false);
        Timer.start();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, final int checkId) {
                if (option1.isChecked() == true || option2.isChecked() == true || option3.isChecked() == true || option4.isChecked() == true) {
                    btnNext.setEnabled(true);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                selection = radioGroup.getCheckedRadioButtonId()!=-1 ? radioButton.getText().toString(): "";
                final String check = selection == map.get(definition.get(num - 1)) ? "True" : "False";
                score = check.equals("True") ? score+=1 : score;

                if(num<20) {
                    Toast.makeText(getApplicationContext(),check,Toast.LENGTH_SHORT).show();
                    radioGroup.clearCheck();
                    if (!isCounterRunning){
                        isCounterRunning = true;
                        Timer.start();
                    }
                    else{
                        Timer.cancel();
                        Timer.start();
                    }
                    refreshPage(definition);
                    btnNext.setEnabled(false);
                    tvScore.setText("Score: "+score+"/20");
                }
                else {
                    btnNext.setText("Submit");
                    if(btnNext.isPressed()){
                        Intent intent = new Intent(Quiz.this, ScoreActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }//end onCreate

    private void refreshPage(ArrayList definition) {
        tvQuestionNum.setText("Question: "+(num+1)+"/20");
        tvQuestion.setText(""+definition.get(num));
        ArrayList<String>term = new ArrayList<>(map.values());
        String answer = map.get(definition.get(num));
        Collections.shuffle(objects);
        Collections.shuffle(term);
        for (int i = 0; i < objects.size(); i++) {
            buttons.get(i).setText(term.get(i));
        }
        int random = (int)(Math.random() * 3 + 1);
        if (option1.getText() != answer && option2.getText() != answer && option3.getText() != answer && option4.getText() != answer) {
            buttons.get(random).setText(answer);
        }
        num++;
    }//end method

    public void onBackPressed() {}

    public static int getScore(){
        return score;
    }

    @Override
    protected void onResume() {
        super.onResume();
        score = 0;
        Toast.makeText(this, "onResume()",Toast.LENGTH_SHORT).show();
    }//end onResume

}//end class