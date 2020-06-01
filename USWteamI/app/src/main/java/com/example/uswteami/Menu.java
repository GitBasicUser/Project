package com.example.uswteami;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Menu extends AppCompatActivity {

    // stt, tts 변수
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextToSpeech myTTS;
    private ImageButton mVoiceBtn;

    // json 변수
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";

    // Intent로 받아오는 변수(Chicken.java로부터)
    private static String shop;
    private static String shopname;
    private String flag_from_chicken;

    // Intent로 받아오는 변수(Menu.java로부터)
    private static ArrayList<String> pay_name = new ArrayList<>();
    private static ArrayList<String> pay_price = new ArrayList<>();
    private static ArrayList<String> pay_content = new ArrayList<>();
    private static int flag = 0;
    private Integer num = flag;

    // json pasing 변수
    int num_main = 1;
    int num_side = 1;
    int num_soda = 1;
    ArrayList<HashMap<String, String>> mArrayList_main;
    ArrayList<HashMap<String, String>> mArrayList_side;
    ArrayList<HashMap<String, String>> mArrayList_soda;
    List<String> names_main = new ArrayList<>();
    List<String> names_side = new ArrayList<>();
    List<String> names_soda = new ArrayList<>();
    ListView mlistView_main;
    ListView mlistView_side;
    ListView mlistView_soda;
    String mJsonString_main;
    String mJsonString_side;
    String mJsonString_soda;

    // Intent 전달 변수
    HashMap<String, String> price_main = new HashMap<>();
    HashMap<String, String> price_side = new HashMap<>();
    HashMap<String, String> price_soda = new HashMap<>();
    HashMap<String, String> content_main = new HashMap<>();
    HashMap<String, String> content_side = new HashMap<>();
    HashMap<String, String> content_soda = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        mVoiceBtn = (ImageButton)findViewById(R.id.voiceBtn);
        
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);


        Intent get = getIntent();
        flag_from_chicken = get.getStringExtra("flag_from_chicken");
        if(flag_from_chicken.equals("y")) {
            shop = get.getStringExtra("shop");
            shopname = get.getStringExtra("shopname");
        }


        if(flag != 0){
            Intent g = getIntent();
            if(!g.getStringExtra("name").equals("no")){
                Log.d("name: ", g.getStringExtra("name"));
                pay_name.add(g.getStringExtra("name"));
                pay_price.add(g.getStringExtra("price"));
                pay_content.add(g.getStringExtra("content"));
            }

            Log.d("flag", get.getStringExtra("flag_delete"));
            if(g.getStringExtra("flag_delete").equals("y")){
                pay_name = (ArrayList<String>) g.getSerializableExtra("pay_name");
                pay_price = (ArrayList<String>) g.getSerializableExtra("pay_price");
                pay_content = (ArrayList<String>) g.getSerializableExtra("pay_content");
            }


        }



        mlistView_main = (ListView) findViewById(R.id.listView_main_list_main);
        mArrayList_main = new ArrayList<>();

        mlistView_side = (ListView) findViewById(R.id.listView_main_list_side);
        mArrayList_side = new ArrayList<>();

        mlistView_soda = (ListView) findViewById(R.id.listView_main_list_soda);
        mArrayList_soda = new ArrayList<>();

        GetData_main task1 = new GetData_main();
        task1.execute("http://uswteami.dothome.co.kr/my/board/chicken/menu/json_main.php");

        GetData_side task2 = new GetData_side();
        task2.execute("http://uswteami.dothome.co.kr/my/board/chicken/menu/json_side.php");

        GetData_soda task3 = new GetData_soda();
        task3.execute("http://uswteami.dothome.co.kr/my/board/chicken/menu/json_soda.php");

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(flag == 0) {
                    String text1 = shopname + " 입니다.";
                    String text2 = "메인메뉴를 원하시면 메인, 사이드메뉴를 원하시면 사이드, 음료를 원하시면 음료를 말해주세요.";
                    String text3 = "원하시는 메뉴의 이름을 말해 주셔도 됩니다.";
                    String text4 = "장바구니 이동은 구매, 다시듣기는 다시, 뒤로이동은 뒤로 입니다.";

                    myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                    myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                    myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
                    myTTS.speak(text4, TextToSpeech.QUEUE_ADD, null);
                }else{
                    String text1 = shopname + " 입니다.";
                    String text2 = "메인, 사이드, 음료, 구매, 다시, 뒤로 중 선택해주세요.";

                    myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                    myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

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

    private class GetData_main extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Menu.this,
                    "Please Wait", null, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {

                mJsonString_main = result;
                showResult_main();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult_main() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString_main);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString("메뉴이름");
                String address = item.getString("가격");
                String where = item.getString("shop");
                String content = item.getString("설명");

                HashMap<String, String> hashMap = new HashMap<>();

                if(shop.equals(where)) {
                    Integer num = num_main++;
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);


                    mArrayList_main.add(hashMap);
                    names_main.add(name);
                    price_main.put(name, address);
                    content_main.put(name, content);
                }
                else continue;
            }

            ListAdapter adapter = new SimpleAdapter(
                    Menu.this, mArrayList_main, R.layout.item_list,
                    new String[]{TAG_ID, TAG_NAME, TAG_ADDRESS},
                    new int[]{R.id.num, R.id.name, R.id.address}
            );

            mlistView_main.setAdapter(adapter);


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    //------------------------------------------------------------------------------------------------------------

    private class GetData_side extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Menu.this,
                    "Please Wait", null, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {
            } else {
                mJsonString_side = result;
                showResult_side();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult_side() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString_side);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString("메뉴이름");
                String address = item.getString("가격");
                String where = item.getString("shop");
                String content = item.getString("설명");

                HashMap<String, String> hashMap = new HashMap<>();

                if(shop.equals(where)) {
                    Integer num = num_side++;
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);


                    mArrayList_side.add(hashMap);
                    names_side.add(name);
                    price_side.put(name,address);
                    content_side.put(name,content);
                }
                else continue;
            }

            ListAdapter adapter = new SimpleAdapter(
                    Menu.this, mArrayList_side, R.layout.item_list,
                    new String[]{TAG_ID, TAG_NAME, TAG_ADDRESS},
                    new int[]{R.id.num, R.id.name, R.id.address}
            );

            mlistView_side.setAdapter(adapter);


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    //------------------------------------------------------------------------------------------------------

    private class GetData_soda extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Menu.this,
                    "Please Wait", null, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {
            } else {

                mJsonString_soda = result;
                showResult_soda();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult_soda() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString_soda);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString("메뉴이름");
                String address = item.getString("가격");
                String where = item.getString("shop");
                String content = item.getString("설명");

                HashMap<String, String> hashMap = new HashMap<>();

                if(shop.equals(where)) {
                    Integer num = num_soda++;
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);


                    mArrayList_soda.add(hashMap);
                    names_soda.add(name);
                    price_soda.put(name, address);
                    content_soda.put(name, content);
                }
                else continue;
            }

            ListAdapter adapter = new SimpleAdapter(
                    Menu.this, mArrayList_soda, R.layout.item_list,
                    new String[]{TAG_ID, TAG_NAME, TAG_ADDRESS},
                    new int[]{R.id.num, R.id.name, R.id.address}
            );

            mlistView_soda.setAdapter(adapter);


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
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
                    String  res = result.get(0).replace(" ", "");

                    if(res.equals("메인")){
                        String text1 = "메인메뉴에는";
                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        for(String n : names_main){
                            myTTS.setSpeechRate(0.95f);
                            myTTS.speak(n + "  ", TextToSpeech.QUEUE_ADD, null);
                        }
                        myTTS.setSpeechRate(1f);
                        myTTS.speak("가 있습니다.다시들으시려면 메인다시, 주문하시려면 원하는 메뉴 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);

                    }else if(res.equals("사이드") || res.equals("싸이드")){
                        String text1 = "사이드메뉴에는";
                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        for(String n : names_side){
                            myTTS.setSpeechRate(0.95f);
                            myTTS.speak(n + "  ", TextToSpeech.QUEUE_ADD, null);
                        }
                        myTTS.setSpeechRate(1f);
                        myTTS.speak("가 있습니다.다시들으시려면 사이드다시, 주문하시려면 원하는 메뉴 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);

                    }else if(res.equals("음료")){
                        String text1 = "음료에는";
                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        for(String n : names_soda){
                            myTTS.setSpeechRate(0.95f);
                            myTTS.speak(n + "  ", TextToSpeech.QUEUE_ADD, null);
                        }
                        myTTS.setSpeechRate(1f);
                        myTTS.speak("가 있습니다.다시들으시려면 음료다시, 주문하시려면 원하는 메뉴 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);


                    }else if(res.equals("다시") || res.equals("-")){
                        String text1 = shopname + " 입니다.";
                        String text2 = "메인메뉴를 원하시면 메인, 사이드메뉴를 원하시면 사이드, 음료를 원하시면 음료를 말해주세요.";
                        String text3 = "장바구니 이동은 구매, 다시듣기는 다시, 뒤로이동은 뒤로 입니다.";

                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                        myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
                    }else if(res.equals("메인다시") || res.equals("메인-")){
                        String text1 = "메인메뉴에는";
                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        for(String n : names_main){
                            myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                        }
                        myTTS.speak("이 있습니다.다시들으시려면 메인다시, 주문하시려면 원하는 메뉴 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);

                    }else if(res.equals("사이드다시") || res.equals("사이드-") || res.equals("싸이드다시") || res.equals("싸이드-")){
                        String text1 = "사이드메뉴에는";
                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        for(String n : names_side){
                            myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                        }
                        myTTS.speak("가 있습니다.다시들으시려면 사이드다시, 주문하시려면 원하는 메뉴 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);

                    }else if(res.equals("음료다시") || res.equals("음료-")){
                        String text1 = "음료에는";
                        myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                        for(String n : names_soda){
                            myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                        }
                        myTTS.speak("가 있습니다.다시들으시려면 음료다시, 주문하시려면 원하는 메뉴 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);

                    }else if(res.equals("구매")){
                        if(pay_name == null) {
                            myTTS.speak("현재 장바구니가 비어 있습니다.메뉴를 먼저 선택해주세요.", TextToSpeech.QUEUE_ADD, null);
                        }
                        Intent i = new Intent(Menu.this, Payment.class);
                        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("name", "장바구니");
                        i.putExtra("pay_name", pay_name);
                        i.putExtra("pay_price", pay_price);
                        i.putExtra("pay_content", pay_content);

                        startActivity(i);
                    }else if(res.equals("뒤로")){
                        Intent i = new Intent(Menu.this, Chicken.class);
                        i.putExtra("flag_from_main", "n");
                        i.putExtra("shop", "n");
                        startActivity(i);
                    }
                    else {
                        for (String n : names_main) {
                            if (res.equals(n)){
                                Intent i = new Intent(Menu.this, Payment.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("name", n);
                                i.putExtra("price", price_main.get(n));
                                i.putExtra("content", content_main.get(n));
                                flag++;
                                startActivity(i);
                            }
                        }

                        for (String n : names_side) {
                            if (res.equals(n)){
                                Intent i = new Intent(Menu.this, Payment.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("name", n);
                                i.putExtra("price", price_side.get(n));
                                i.putExtra("content", content_side.get(n));
                                flag++;
                                startActivity(i);
                            }
                        }

                        for (String n : names_soda) {
                            if (res.equals(n)){
                                Intent i = new Intent(Menu.this, Payment.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("name", n);
                                i.putExtra("price", price_soda.get(n));
                                i.putExtra("content", content_soda.get(n));
                                flag++;
                                startActivity(i);
                            }
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mVoiceBtn.performClick();
                        }
                    }, 1000);

                    break;
                }else{
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
    protected void onStop() {
        super.onStop();
        if (myTTS != null){
            myTTS.stop();
            myTTS.shutdown();
        }
    }

}
