package com.example.uswteami;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;

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
    private static String sttHow = "n";

    private static ArrayList<String> pay_name = new ArrayList<>();
    private static ArrayList<String> pay_price = new ArrayList<>();
    private static ArrayList<String> pay_content = new ArrayList<>();
    private static ArrayList<String> shops = new ArrayList<>();
    private static ArrayList<String> qw = new ArrayList<>();
    private static String ad = "경기도 화성시 봉담읍 와우안길 17";
    int k = 0;
    Button aa, aaa;
    private static int a =0;
    ArrayList<String> menus = new ArrayList<>();
    int b = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVoiceBtn = findViewById(R.id.voiceBtn);
        settingBtn = findViewById(R.id.settingBtn);
        chicken = findViewById(R.id.btn_chicken);
        aa = (Button)findViewById(R.id.aa);
        aaa = (Button)findViewById(R.id.a);
        btns.add(mVoiceBtn);
        btns.add(settingBtn);
        btns.add(chicken);
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);
        menus.add("치킨");menus.add("피자");menus.add("중식");
        menus.add("카페");menus.add("일식");menus.add("분식");
        menus.add("보쌈");menus.add("족발");menus.add("야식");
        menus.add("찜, 탕");menus.add("도시락");menus.add("한식");
        menus.add("양식");menus.add("돈까스");menus.add("햄버거");

        Intent get = getIntent();
        if(n != 0) {
            if(get.getStringExtra("flag_from_Review").equals("y")){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();

                shops = (ArrayList<String>) get.getSerializableExtra("shops");

                setStringArrayPref(MainActivity.this, "shop", shops);

                editor.apply();
            }
            else if (get.getStringExtra("flag_from_Payment").equals("y")) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                String q = "y";

                pay_name = (ArrayList<String>) get.getSerializableExtra("name");
                pay_price = (ArrayList<String>) get.getSerializableExtra("price");
                pay_content = (ArrayList<String>) get.getSerializableExtra("content");

                if(qw.size() == 0)
                    qw.add(get.getStringExtra("shop"));
                else
                    qw.set(0, get.getStringExtra("shop"));

                for(String n : shops){
                    if(n.equals(get.getStringExtra("shop"))){
                        q = "n";
                    }
                }
                if(q.equals("y")) {
                    shops.add(get.getStringExtra("shop"));
                }

                k = get.getIntExtra("k", 0);
                editor.putInt("k", k);

                setStringArrayPref(MainActivity.this, "name", pay_name);
                setStringArrayPref(MainActivity.this, "price", pay_price);
                setStringArrayPref(MainActivity.this, "content", pay_content);
                setStringArrayPref(MainActivity.this, "shop", shops);
                setStringArrayPref(MainActivity.this, "qw", qw);

                editor.apply();

            } else {
                place = get.getStringExtra("place");
            }
        }


        if(sttHow.equals("y")){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    player_s.start();
                    mVoiceBtn.performClick();
                }
            }, 1000);

            //button click to show speech to text dialog 텍스트 대화 상자에 음성을 표시하려면 버튼 클릭
            mVoiceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player_s.start();
                    speak();
                }
            });

        }else if(sttSwitch.equals("y")) {
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
                                player_s.start();
                                mVoiceBtn.performClick();
                            }
                        }, 14500);
                    }else{
                        String myText = "설정, 주문, 또는 사용법 을 말씀해주세요.";

                        myTTS.speak(myText, TextToSpeech.QUEUE_ADD, null);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player_s.start();
                                mVoiceBtn.performClick();
                            }
                        }, 5000);
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

        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                pay_name = getStringArrayPref(MainActivity.this, "name");
                pay_price = getStringArrayPref(MainActivity.this, "price");
                pay_content = getStringArrayPref(MainActivity.this, "content");
                shops = getStringArrayPref(MainActivity.this, "shop");
                qw = getStringArrayPref(MainActivity.this, "qw");

                k = prefs.getInt("k", 0);

                if(k == 0){
                    Toast.makeText(MainActivity.this, "주문 내역이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }else{
                    Intent i = new Intent(MainActivity.this, Payment.class);
                    i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("name", "장바구니");
                    i.putExtra("pay_name", pay_name);
                    i.putExtra("pay_price", pay_price);
                    i.putExtra("pay_content", pay_content);
                    i.putExtra("sttSwitch", sttSwitch);
                    i.putExtra("sttHow", sttHow);
                    i.putExtra("shop", qw.get(0));
                    i.putExtra("where", "main");

                    startActivity(i);
                }
            }
        });

        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                shops = getStringArrayPref(MainActivity.this, "shop");
                k = prefs.getInt("k", 0);

                if(k == 0){
                    Toast.makeText(MainActivity.this, "주문 내역이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }else{
                    Intent i = new Intent(MainActivity.this, Review.class);
                    i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("shop", shops);
                    i.putExtra("place", place);
                    i.putExtra("sttSwitch", sttSwitch);
                    i.putExtra("sttHow", sttHow);
                    i.putExtra("flag_from_main", "y");
                    startActivity(i);
                }

            }
        });
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
                    i.putExtra("sttHow", sttHow);
                    startActivity(i);
                    break;
                case R.id.btn_chicken:
                    n++;
                    Intent j = new Intent(MainActivity.this, Chicken.class);
                    j.setFlags(j.FLAG_ACTIVITY_CLEAR_TOP);
                    j.putExtra("place", place);
                    j.putExtra("shop", "치킨");
                    j.putExtra("address", ad);
                    j.putExtra("flag_from_main", "y");
                    j.putExtra("sttSwitch", sttSwitch);
                    j.putExtra("sttHow", sttHow);
                    startActivity(j);
                    break;
                case R.id.voiceBtn:
                    sttSwitch="y";
                    speak();
                    break;
            }
        }
    };

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
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
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 14500);
                            }else{
                                String myText = "설정, 주문, 또는 사용법 을 말씀해주세요.";

                                myTTS.speak(myText, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 4500);
                            }
                        } else if (result.get(0).equals("주문") || result.get(0).equals("9분") || result.get(0).equals("휴먼")) {
                            if(flag == 0) {
                                String order1 = "주문을 원하시면 카테고리에서 메뉴를 말씀해주세요.";
                                String order2 = "주문 카테고리 확인은 다음,  ";
                                String order3 = "최근 주문하신 메뉴를 다시 주문하시려면 주문내역 을 말해주세요.";

                                myTTS.speak(order1, TextToSpeech.QUEUE_FLUSH, null);
                                myTTS.speak(order2, TextToSpeech.QUEUE_ADD, null);
                                myTTS.speak(order3, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 11000);
                            }else{
                                String order1 = "다음, 또는 주문내역을 말해주세요.";
                                myTTS.speak(order1, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 4500);
                            }
                        } else if (result.get(0).equals("사용법")) {
                            String text1 = "해당 배달앱은 음성인식을 적용하여 특정 명령어들로 주문이 가능한 배달앱 입니다.";
                            String text2 = "명령어 입력 후, 안내가 나오지 않고 알림음이 다시 나온다면, 해당 명령어를 천천히 다시 말해주시기 바랍니다.";
                            String text3 = "안내로 나오지 않아도 항상 적용되는 명령어에는";
                            String text4 = "이전페이지로 돌아가게 해주는 이전, 안내를 한번 더 들려주는 다시 가 있습니다.";
                            String text5 = "주문을 1회이상 하신 후, 재실행하여 다시 주문을 원하시면 주문내역, 리뷰를 작성하시려면 리뷰를 말해주세요.";
                            String text6 = "음성인식을 종료하고싶으시면 종료, 음성인식을 재실행 하고 싶으시면 메인메뉴의 오른쪽 위 마이크버튼을 눌러주세요.";
                            String text7 = "음성안내를 받지 않으시고 음성인식을 사용하여 주문하시려면 생략 을 말헤주세요.";

                            myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
                            myTTS.setSpeechRate(0.95f);
                            myTTS.speak(text4, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text5, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text6, TextToSpeech.QUEUE_ADD, null);
                            myTTS.speak(text7, TextToSpeech.QUEUE_ADD, null);
                            myTTS.setSpeechRate(1f);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player_s.start();
                                    mVoiceBtn.performClick();
                                }
                            }, 55000);
                        } else if (result.get(0).equals("종료")) {
                            sttSwitch = "n";
                            Toast.makeText(MainActivity.this, "음성인식을 종료합니다.", Toast.LENGTH_LONG).show();
                        } else if(result.get(0).equals("생략") || result.get(0).equals("학력")){
                            sttHow = "y";

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player_s.start();
                                    mVoiceBtn.performClick();
                                }
                            }, 1000);
                        } else if(result.get(0).equals("주문내역")){
                            n++;
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                            pay_name = getStringArrayPref(MainActivity.this, "name");
                            pay_price = getStringArrayPref(MainActivity.this, "price");
                            pay_content = getStringArrayPref(MainActivity.this, "content");
                            shops = getStringArrayPref(MainActivity.this, "shop");
                            qw = getStringArrayPref(MainActivity.this, "qw");

                            k = prefs.getInt("k", 0);

                            if(k == 0){
                                String order = "주문 내역이 존재하지 않습니다.";
                                myTTS.speak(order, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 1500);
                            }else{
                                Intent i = new Intent(MainActivity.this, Payment.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("name", "장바구니");
                                i.putExtra("pay_name", pay_name);
                                i.putExtra("pay_price", pay_price);
                                i.putExtra("pay_content", pay_content);
                                i.putExtra("sttSwitch", sttSwitch);
                                i.putExtra("sttHow", sttHow);
                                i.putExtra("shop", qw.get(0));
                                i.putExtra("where", "main");

                                startActivity(i);
                            }

                        }else if(result.get(0).equals("다음")){
                            if(b > menus.size()) b = 0;
                            int s = 0;

                            for(int i = 0;i < 3; i++){
                                myTTS.setSpeechRate(0.95f);
                                myTTS.speak(menus.get(b), TextToSpeech.QUEUE_ADD, null);
                                s += menus.get(b).length();
                                b++;
                            }

                            String order = "원하시는 주문 카테고리를 말해주세요.";
                            myTTS.setSpeechRate(1f);
                            myTTS.speak(order, TextToSpeech.QUEUE_ADD, null);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player_s.start();
                                    mVoiceBtn.performClick();
                                }
                            }, 2700 + s*550);
                        }
                        else if(result.get(0).equals("리뷰")){
                            n++;
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            shops = getStringArrayPref(MainActivity.this, "shop");
                            k = prefs.getInt("k", 0);

                            if(k == 0){
                                String order = "리뷰가능한 음식점이 없습니다.";
                                myTTS.speak(order, TextToSpeech.QUEUE_ADD, null);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        player_s.start();
                                        mVoiceBtn.performClick();
                                    }
                                }, 2000);
                            }else{
                                Intent i = new Intent(MainActivity.this, Review.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("shop", shops);
                                i.putExtra("place", place);
                                i.putExtra("sttSwitch", sttSwitch);
                                i.putExtra("sttHow", sttHow);
                                i.putExtra("flag_from_main", "y");
                                startActivity(i);
                            }

                        }
                        else {
                            sttSwitch = "y";
                            btsClick(result.get(0));
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

    private void btsClick (String text) {
        flag++;
        if(sttSwitch.equals("y")) {
            final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);
            if (text.equals("설정")) {
                a = 1;
                n++;
                Intent i = new Intent(MainActivity.this, Setting.class);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("place", place);
                i.putExtra("sttSwitch", sttSwitch);
                i.putExtra("sttHow", sttHow);
                startActivity(i);

            } else if (text.equals("치킨") || text.equals("피자")) {
                n++;
                a = 1;
                Intent j = new Intent(MainActivity.this, Chicken.class);
                j.setFlags(j.FLAG_ACTIVITY_CLEAR_TOP);
                j.putExtra("shop", text);
                j.putExtra("flag_from_main", "y");
                j.putExtra("address", ad);
                j.putExtra("place", place);
                j.putExtra("sttSwitch", sttSwitch);
                j.putExtra("sttHow", sttHow);
                startActivity(j);

            } else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        player_s.start();
                        mVoiceBtn.performClick();
                    }
                }, 1000);
            }
        }
    }

}
