package com.example.uswteami;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnInitListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private String place = "앱 지정순";
    private static Integer n = 0;

    //views from activity
    private TextView mTextTv;
    private ImageButton mVoiceBtn;
    private Button settingBtn;
    private Button chicken;
    private TextView test;

    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTTS = new TextToSpeech(this, this);
        mTextTv = findViewById(R.id.textTv);
        mVoiceBtn = findViewById(R.id.voiceBtn);
        settingBtn = findViewById(R.id.settingBtn);
        chicken = findViewById(R.id.btn_chicken);
        test = (TextView) findViewById(R.id.testText);
        
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);

        Log.d("n: ", n.toString());
        //settingBtn.setOnClickListener(onClick);
        if (n != 0) {
            Intent get = getIntent();
            place = get.getStringExtra("place");
        }
        test.setText(place);

        //button click to show speech to text dialog 텍스트 대화 상자에 음성을 표시하려면 버튼 클릭
        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_s.start();
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

    @Override
    public void onInit(int status) {
        String myText1 = "음성인식 배달 앱 입니다.";
        String myText2 = "설정을 원하시면 설정, 주문을 원하시면 주문을 말씀해주세요.";
        String myText3 = "다시듣기는 다시 를 말씀해주세요.";

        myTTS.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
        myTTS.speak(myText2, TextToSpeech.QUEUE_ADD, null);
        myTTS.speak(myText3, TextToSpeech.QUEUE_ADD, null);
    }


//    View.OnClickListener onClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.settingBtn:
//                    n++;
//                    Intent i = new Intent(MainActivity.this, Setting.class);
//                    i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.putExtra("place", place);
//                    startActivity(i);
//                    break;
//                case R.id.btn_chicken:
//                    Intent j = new Intent(MainActivity.this, Chicken.class);
//                    j.setFlags(j.FLAG_ACTIVITY_CLEAR_TOP);
//                    j.putExtra("place", place);
//                    startActivity(j);
//                    break;
//            }
//        }
//    };


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
        
        final MediaPlayer player_f = MediaPlayer.create(this, R.raw.finish);

        player_f.start();


        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intent 음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text view 텍스트 보기로 설정
                    mTextTv.setText(result.get(0));
                    btsClick(result.get(0));
                    if (result.get(0).equals("다시")) {
                        onInit(0);
                    }
                    else if (result.get(0).equals("주문")){
                        String order1 = "주문을 원하시면 카테고리에서 메뉴를 말씀해주세요.";
                        String order2 = "주문 카테고리에는 치킨, 피자 가 있습니다.";
                        String order3 = "한번 더 듣기를 원하시면 '주문다시'라고 말씀해주세요";

                        myTTS.speak(order1, TextToSpeech.QUEUE_FLUSH, null);
                        myTTS.speak(order2, TextToSpeech.QUEUE_ADD, null);
                        myTTS.speak(order3, TextToSpeech.QUEUE_ADD, null);

                    }
                    else if (result.get(0).equals("주문-")){
                        String order1 = "주문을 원하시면 카테고리에서 메뉴를 말씀해주세요.";
                        String order2 = "주문 카테고리에는";
                        String order3 = "치킨, 피자 가 있습니다.";
                        String order4 = "한번 더 듣기를 원하시면 '주문다시'라고 말씀해주세요";

                        myTTS.speak(order1, TextToSpeech.QUEUE_FLUSH, null);
                        myTTS.speak(order2, TextToSpeech.QUEUE_ADD, null);
                        myTTS.speak(order3, TextToSpeech.QUEUE_ADD, null);
                        myTTS.speak(order4, TextToSpeech.QUEUE_ADD, null);
                    }
                    break;
                }
            }


        }
    }

        @Override
        protected void onStop () {
            super.onStop();
            if (myTTS != null) {
                myTTS.stop();
                myTTS.shutdown();
            }
        }

    private void btsClick (String text){
        if (text.equals("설정")) {
            n++;
            Intent i = new Intent(MainActivity.this, Setting.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("place", place);
            startActivity(i);
        } else if (text.equals("치킨") || text.equals("피자")) {
            Intent j = new Intent(MainActivity.this, Chicken.class);
            j.setFlags(j.FLAG_ACTIVITY_CLEAR_TOP);
            j.putExtra("shop", text);
            j.putExtra("flag_from_main", "y");
            j.putExtra("place", place);
            startActivity(j);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVoiceBtn.performClick();
                }
            }, 1000);
        }
    }
}
