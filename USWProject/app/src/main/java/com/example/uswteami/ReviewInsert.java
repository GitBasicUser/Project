package com.example.uswteami;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReviewInsert extends AppCompatActivity {

    ArrayList arr = new ArrayList<>();
    ArrayList<String> shops = new ArrayList<>();
    ArrayAdapter adapter;
    String stt;
    String stth;
    EditText review;
    Button set;
    Spinner spi;
    String sh;
    String star = "n";
    String reviewStart = "n";

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextToSpeech myTTS;
    private ImageButton mVoiceBtn;

    String data1 = null;
    String data2 = null;
    String data3 = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewinsert);

        spi = (Spinner)findViewById(R.id.spi);
        review = (EditText)findViewById(R.id.reviewText);
        set = (Button)findViewById(R.id.set);

        Intent get = getIntent();
        sh = get.getStringExtra("shop");
        shops = (ArrayList<String>) get.getSerializableExtra("shops");
        stt = get.getStringExtra("sttSwitch");
        stth = get.getStringExtra("sttHow");
        mVoiceBtn = findViewById(R.id.voiceBtn);
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);

        arr.add("0");
        arr.add("1");
        arr.add("2");
        arr.add("3");
        arr.add("4");
        arr.add("5");

        adapter = new ArrayAdapter(ReviewInsert.this, android.R.layout.simple_spinner_dropdown_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spi.setPrompt("선택");
        spi.setSelection(0);
        spi.setAdapter(adapter);


        NetworkUtil.setNetworkPolicy();

        final String[] d = new String[1];

        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                d[0] = spi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1 = sh;
                data2 = review.getText().toString();
                data3 = d[0];

                try {
                    PHPRequest request = new PHPRequest("http://uswteami.dothome.co.kr/my/review/shop/androidApply.php");
                    String result = request.PhPtest(data1, data2, data3);
                    if(result.equals("1")){
                        Toast.makeText(getApplication(),"리뷰 저장 완료",Toast.LENGTH_SHORT).show();
                    }

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }

                Intent i = new Intent(ReviewInsert.this, MainActivity.class);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("flag_from_Review", "y");
                i.putExtra("shops", shops);
                i.putExtra("flag_from_Payment", "n");
                startActivity(i);

            }
        });

        if(stt.equals("y")){
            myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    String text1 = sh + "의 리뷰입니다.별점작성은 별점, 리뷰작성은 리뷰, 리뷰등록은 저장 을 말해주세요.";
                    myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player_s.start();
                            mVoiceBtn.performClick();
                        }
                    }, 7000);

                    mVoiceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            player_s.start();
                            speak();
                        }
                    });
                }
            });
        }

    }

    public static class NetworkUtil {
        @SuppressLint("NewApi")
        static public void setNetworkPolicy() {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        }
    }

    public class PHPRequest {
        private URL url;

        public PHPRequest(String url) throws MalformedURLException { this.url = new URL(url); }

        private String readStream(InputStream in) throws IOException {
            StringBuilder jsonHtml = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;

            while((line = reader.readLine()) != null) {
                //Toast.makeText(getApplication(),line,Toast.LENGTH_SHORT).show();
                jsonHtml.append(line);
            }

            reader.close();
            return jsonHtml.toString();
        }

        public String PhPtest(final String data1, final String data2,final String data3) {
            try {
                String postData = "Data1=" + data1 + "&" + "Data2=" + data2 + "&" + "Data3=" + data3;
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                String result = readStream(conn.getInputStream());
                conn.disconnect();
                return result;
            }
            catch (Exception e) {
                Log.i("PHPRequest", "request was failed.");
                return null;
            }
        }
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
                    if(stt.equals("y")) {
                        if(star.equals("y")){
                            if(result.get(0).equals("0") || result.get(0).equals("영")) {
                                data3 = result.get(0);
                                star = "n";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 1000);
                            }
                            else if(result.get(0).equals("1") || result.get(0).equals("일")){
                                data3 = result.get(0);
                                star = "n";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 1000);
                            }
                            else if(result.get(0).equals("이") || result.get(0).equals("2")) {
                                data3 = result.get(0);
                                star = "n";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 1000);
                            }
                            else if(result.get(0).equals("3") || result.get(0).equals("삼")) {
                                data3 = result.get(0);
                                star = "n";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 1000);
                            }
                            else if(result.get(0).equals("4") || result.get(0).equals("사")) {
                                data3 = result.get(0);
                                star = "n";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 1000);
                            }
                            else if(result.get(0).equals("5") || result.get(0).equals("오")) {
                                data3 = result.get(0);
                                star = "n";
                            }else{
                                String text1 = "0 부터 5 까지 별점 개수를 정수로 말해주세요.";
                                myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 2500);
                            }
                        }else if (reviewStart.equals("y")){
                            data2 = result.get(0);

                            String text1 = "리뷰작성이 완료되었습니다.";
                            String text2 = "작성내용 확인은 확인, 리뷰등록은 저장 입니다.";

                            myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player_s.start();
                                    mVoiceBtn.performClick();
                                }
                            }, 5500);

                            reviewStart = "n";
                        }
                        else if(result.get(0).equals("별점")){
                            star = "y";
                            String text1 = "0 부터 5 까지 별점 개수를 정수로 말해주세요.";
                            myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player_s.start();
                                    mVoiceBtn.performClick();
                                }
                            }, 4500);
                        }
                        else if(result.get(0).equals("리뷰")){
                            reviewStart = "y";
                            String text1 = "리뷰작성을 시작합니다.";
                            myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player_s.start();
                                    mVoiceBtn.performClick();
                                }
                            }, 2500);
                        }else if(result.get(0).equals("확인")){
                            if(data2 == null){
                                String text1 = "리뷰를 먼저 작성해주세요.";
                                myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 2500);
                            }else{
                                int s = 0;
                                s = data2.length();
                                myTTS.speak(data2, TextToSpeech.QUEUE_ADD, null);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, s*600);
                            }
                        }else if(result.get(0).equals("저장")){
                            if(data2 == null){
                                String text1 = "별점 명령어를 이용해서 별점을 먼저 등록해주세요.";
                                myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 4500);
                            }else if(data3 == null){
                                String text1 = "리뷰 명령어를 이용해서 리뷰를 먼저 작성해주세요.";
                                myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 4500);
                            }else{
                                data1 = sh;
                                try {
                                    PHPRequest request = new PHPRequest("http://uswteami.dothome.co.kr/my/review/shop/androidApply.php");
                                    String resulk = request.PhPtest(data1, data2, data3);
                                    if(resulk.equals("1")){
                                        String text1 = "리뷰 등록이 완료되었습니다.";
                                        myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                                    }

                                }catch (MalformedURLException e){
                                    e.printStackTrace();
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(ReviewInsert.this, MainActivity.class);
                                        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.putExtra("flag_from_Review", "y");
                                        i.putExtra("shops", shops);
                                        i.putExtra("flag_from_Payment", "n");
                                        startActivity(i);
                                    }
                                }, 2000);
                            }
                        }
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

}

