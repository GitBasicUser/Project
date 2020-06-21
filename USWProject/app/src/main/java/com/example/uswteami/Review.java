package com.example.uswteami;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Review extends AppCompatActivity {

    private static ArrayList<String> shops = new ArrayList<>();
    ListView list;
    ListViewAdapter adapter;
    private static String mJsonString;
    private static final String TAG_JSON = "webnautes";
    private static final String TAG = "";
    private String stt;
    private String stth;
    private String place;
    ImageButton back;
    Integer num = 0;
    String name = null;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextToSpeech myTTS;
    private ImageButton mVoiceBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        list = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter();
        back = (ImageButton) findViewById(R.id.back);
        mVoiceBtn = findViewById(R.id.voiceBtn);

        Intent get = getIntent();
        if(get.getStringExtra("flag_from_main").equals("y")){
            shops = (ArrayList<String>) get.getSerializableExtra("shop");
            stt = get.getStringExtra("sttSwitch");
            stth = get.getStringExtra("sttHow");
            place = get.getStringExtra("place");
        }
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);

        list.setAdapter(adapter);
        Integer num = 0;
        for(String n : shops){
            num++;
            adapter.addItem(num.toString(), n, "");
        }

        if (stt.equals("y")) {
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    int s = 0;
                    String text1 = "리뷰가능한 음식점은";
                    myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);

                    for(String n : shops){
                        myTTS.setSpeechRate(0.95f);
                        myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                        s++;
                    }
                    myTTS.setSpeechRate(1f);
                    myTTS.speak("가 있습니다.", TextToSpeech.QUEUE_ADD, null);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player_s.start();
                            mVoiceBtn.performClick();
                        }
                    }, 2000+2000*s);

                }
            });
        }


        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_s.start();
                speak();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Review.this, MainActivity.class);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("place", place);
                i.putExtra("shops", shops);
                i.putExtra("flag_from_Review", "y");
                i.putExtra("flag_from_Payment", "n");
                startActivity(i);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                list = (ListView)findViewById(R.id.listView);
                adapter = new ListViewAdapter();
                list.setAdapter(adapter);

                Intent i = new Intent(Review.this, ReviewInsert.class);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("shop", shops.get(position));

                shops.remove(position);

                Integer num = 0;
                for(String n : shops){
                    num++;
                    adapter.addItem(num.toString(), n, "");
                }

                i.putExtra("shops", shops);
                i.putExtra("sttSwitch", stt);
                startActivity(i);
            }
        });
    }

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
                    final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String res = result.get(0);

                    if(res.equals("이전")){
                        Intent i = new Intent(Review.this, MainActivity.class);
                        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("place", place);
                        i.putExtra("flag_from_Payment", "n");
                        startActivity(i);
                    }
                    for(String n : shops){
                        if(res.equals(n)){
                            name = n;
                        }
                    }
                    if(name != null){
                        list = (ListView)findViewById(R.id.listView);
                        adapter = new ListViewAdapter();
                        list.setAdapter(adapter);

                        Intent i = new Intent(Review.this, ReviewInsert.class);
                        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("shop", name);

                        shops.remove(name);

                        Integer num = 0;
                        for(String k : shops){
                            num++;
                            adapter.addItem(num.toString(), k, "");
                        }
                        name = null;

                        i.putExtra("shops", shops);
                        i.putExtra("sttSwitch", stt);
                        startActivity(i);
                    }

                    break;
                }
            }


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
        }
    }

}
