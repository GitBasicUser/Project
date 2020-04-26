package com.example.uswteami;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private String place = " 별점순";
    private static Integer n = 0;

    //views from activity
    private TextView mTextTv;
    private ImageButton mVoiceBtn;
    private Button settingBtn;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextTv = findViewById(R.id.textTv);
        mVoiceBtn = findViewById(R.id.voiceBtn);
        settingBtn = findViewById(R.id.settingBtn);
        test = (TextView) findViewById(R.id.testText);

        settingBtn.setOnClickListener(onClick);
        if (n != 0) {
            Intent get = getIntent();
            place = get.getStringExtra("place");
            test.setText(place);
        }

        //button click to show speech to text dialog 텍스트 대화 상자에 음성을 표시하려면 버튼 클릭
        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVoiceBtn.performClick();
            }
        }, 500);


    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.settingBtn:
                    n++;
                    Intent i = new Intent(MainActivity.this, Setting.class);
                    i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("place", place);
                    startActivity(i);
                    break;
            }
        }
    };


    private void speak() {
        //intent to show speech to text dialog 텍스트 대화 상자에 음성 표시
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        //start intent 인텐트 시작
        try {
            //in there was no error
            //show dialog
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        } catch (Exception e) {
            //if there was some error
            //get message of error and show
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    //receive voice input and handle it 음성을 입력 받아 처리

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intent 음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text view 텍스트 보기로 설정
                    mTextTv.setText(result.get(0));
                    btsClick(result.get(0));
                }
                break;
            }
        }


    }

    private void btsClick(String text){
        if (text.equals(settingBtn.getText().toString())){
            n++;
            Intent i = new Intent(MainActivity.this, Setting.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("place", place);
            startActivity(i);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVoiceBtn.performClick();
                }
            }, 1000);
        }
    }


}