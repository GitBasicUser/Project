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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private String place = "앱 지정순";
    private static Integer n = 0;
    private static int flag = 0;

    //views from activity
    private ImageButton mVoiceBtn;
    private ImageButton settingBtn;
    private ImageButton chicken;
    private List<ImageButton> btns = new ArrayList<>();

    private TextToSpeech myTTS;
    private static String sttSwitch = "y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVoiceBtn = findViewById(R.id.voiceBtn);
        settingBtn = findViewById(R.id.settingBtn);
        chicken = findViewById(R.id.btn_chicken);
        btns.add(mVoiceBtn);
        btns.add(settingBtn);
        btns.add(chicken);


        if (n != 0) {
            Intent get = getIntent();
            place = get.getStringExtra("place");
        }
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);

        if(sttSwitch.equals("y")) {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(flag == 0) {
                        String myText1 = "음성인식 배달 앱 입니다.";
                        String myText2 = "안내가 끝난 후, 알림음이 나오면 명령어를 말해주세요. 처음 사용하실경우 반드시 사용법 을 말하여 안내를 들어주시기 바랍니다.";
                        String myText3 = "설정, 주문, 또는 사용법 을 말씀해주세요.";

                        myTTS.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
                        myTTS.speak(myText2, TextToSpeech.QUEUE_ADD, null);
                        myTTS.speak(myText3, TextToSpeech.QUEUE_ADD, null);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mVoiceBtn.performClick();
                            }
                        }, 14500);
                    }else{
                        String myText = "설정, 주문, 또는 사용법 을 말씀해주세요.";

                        myTTS.speak(myText, TextToSpeech.QUEUE_ADD, null);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mVoiceBtn.performClick();
                            }
                        }, 4000);
                    }
                }
            });

            //button click to show speech to text dialog 텍스트 대화 상자에 음성을 표시하려면 버튼 클릭
            mVoiceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player_s.start();
                    speak();
                }
            });


        }
        for(int i = 0; i<btns.size(); i++){
            btns.get(i).setOnClickListener(onClick);
        }
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
                    i.putExtra("sttSwitch", sttSwitch);
                    startActivity(i);
                    break;
                case R.id.btn_chicken:
                    Intent j = new Intent(MainActivity.this, Chicken.class);
                    j.setFlags(j.FLAG_ACTIVITY_CLEAR_TOP);
                    j.putExtra("place", place);
                    j.putExtra("shop", "치킨");
                    j.putExtra("flag_from_main", "y");
                    j.putExtra("sttSwitch", sttSwitch);
                    startActivity(j);
                    break;
                case R.id.voiceBtn:
                    sttSwitch="y";
                    speak();
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

        final MediaPlayer player_f = MediaPlayer.create(this, R.raw.finish);

        player_f.start();


        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(sttSwitch.equals("y")) {
                        if (result.get(0).equals("다시") || result.get(0).equals("-")) {
                            if(flag == 0) {
                                String myText1 = "음성인식 배달 앱 입니다.";
                                String myText2 = "안내가 끝난 후, 알림음이 나오면 명령어를 말해주세요.처음 사용하실경우 반드시 사용법 을 말하여 안내를 들어주시기 바랍니다.";
                                String myText3 = "설정, 주문, 또는 사용법 을 말씀해주세요.";

                                myTTS.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
                                myTTS.speak(myText2, TextToSpeech.QUEUE_ADD, null);
                                myTTS.speak(myText3, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mVoiceBtn.performClick();
                                    }
                                }, 14500);
                            }else{
                                String myText = "설정, 주문, 또는 사용법 을 말씀해주세요.";

                                myTTS.speak(myText, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mVoiceBtn.performClick();
                                    }
                                }, 4500);
                            }
                        } else if (result.get(0).equals("주문")) {
                            String order1 = "주문을 원하시면 카테고리에서 메뉴를 말씀해주세요.";
                            String order2 = "주문 카테고리에는 치킨, 피자 가 있습니다.";

                            myTTS.speak(order1, TextToSpeech.QUEUE_FLUSH, null);
                            myTTS.speak(order2, TextToSpeech.QUEUE_ADD, null);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mVoiceBtn.performClick();
                                }
                            }, 8500);

                        } else if (result.get(0).equals("사용법")) {
                            String text1 = "해당 배달앱은 음성인식을 적용하여 특정 명령어들로 주문이 가능한 배달앱 입니다.";
                            String text2 = "명령어 입력 후, 안내가 나오지 않고 알림음이 다시 나온다면, 해당 명령어를 천천히 다시 말해주시기 바랍니다.";
                            String text3 = "안내로 나오지 않아도 항상 적용되는 명령어에는";
                            String text4 = "이전페이지로 돌아가게 해주는 이전, 안내를 한번 더 들려주는 다시 가 있습니다.";
                            String text5 = "음성인식을 종료하고싶으시면 종료, 음성인식을 재실행 하고 싶으시면 오른쪽 위 마이크버튼을 누른 후, 실행 을 말해주세요.";

                            myTTS.setSpeechRate(0.95f);
                            myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text4, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text5, TextToSpeech.QUEUE_ADD, null);
                            myTTS.setSpeechRate(1f);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mVoiceBtn.performClick();
                                }
                            }, 48000);
                        } else if (result.get(0).equals("종료")) {
                            sttSwitch = "n";
                        } else {
                            sttSwitch = "y";
                        }
                        btsClick(result.get(0));
                    }
                    else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mVoiceBtn.performClick();
                            }
                        }, 1000);
                    }
                    break;
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mVoiceBtn.performClick();
                        }
                    }, 1000);
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

    private void btsClick (String text) {
        flag++;
        if (text.equals("설정")) {
            n++;
            Intent i = new Intent(MainActivity.this, Setting.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("place", place);
            i.putExtra("sttSwitch", sttSwitch);
            startActivity(i);
        } else if (text.equals("치킨") || text.equals("피자")) {
            Intent j = new Intent(MainActivity.this, Chicken.class);
            j.setFlags(j.FLAG_ACTIVITY_CLEAR_TOP);
            j.putExtra("shop", text);
            j.putExtra("flag_from_main", "y");
            j.putExtra("place", place);
            j.putExtra("sttSwitch", sttSwitch);
            startActivity(j);
        }

    }

}
