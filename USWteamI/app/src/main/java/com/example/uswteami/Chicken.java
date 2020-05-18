package com.example.uswteami;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
=======
>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa

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
<<<<<<< HEAD
import java.util.Locale;

public class Chicken extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
=======

public class Chicken extends AppCompatActivity {

>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa
    private TextToSpeech myTTS;
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";


<<<<<<< HEAD
    private ImageButton mVoiceBtn;
    private TextView place_layout;
    ArrayList<HashMap<String, String>> mArrayList;
    HashMap<String,String> shops = new HashMap<>();
=======
    private TextView place_layout;
    ArrayList<HashMap<String, String>> mArrayList;
>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa
    List<String> names = new ArrayList<>();
    ListView mlistView;
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken);

        place_layout = (TextView)findViewById(R.id.placeLayout);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();
        mVoiceBtn = (ImageButton)findViewById(R.id.voiceBtn);

        myTTS = new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                String Text = "치킨 카테고리입니다.";
                myTTS.speak(Text, TextToSpeech.QUEUE_FLUSH, null);

                for(String n : names){
                    myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

        myTTS = new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                String Text = "치킨 카테고리입니다.";
                myTTS.speak(Text, TextToSpeech.QUEUE_FLUSH, null);

                for(String n : names){
                    myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });


        String place;
        Intent get = getIntent();
        place = get.getStringExtra("place");
        if(place == null){
            Log.v("p: ","null");
        }else {
            Log.v("p: ", place);
            place_layout.setText(place);
        }
        GetData task = new GetData();
<<<<<<< HEAD
        task.execute("http://uswteami.dothome.co.kr/my/board/chicken/json.php");

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

=======
        task.execute("http://uswteami.dothome.co.kr/test/board/chicken/json.php");


    }

>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Chicken.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null){

            }
            else {

                mJsonString = result;
                showResult();
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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intent 음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text view 텍스트 보기로 설정
                    for(String n : names){
                        if(result.get(0).equals(n)){
                            Intent i = new Intent(Chicken.this,Menu.class);
                            i.putExtra("shop", shops.get(n));
                            startActivity(i);
                        }
                    }
                    if(result.get(0).equals("뒤로")){
                        Intent i = new Intent(Chicken.this, MainActivity.class);
                        startActivity(i);
                    }
                    break;
                }
            }


        }


    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

<<<<<<< HEAD
                Integer num = i + 1;
                String name = item.getString("가게이름");
=======
                String id = item.getString("PrimaryKey");
                String name = item.getString("가게이름"); //가게변수, key 값저장

                myTTS.speak(name, TextToSpeech.QUEUE_FLUSH, null);

>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa
                String address = item.getString("주소");
                String shop = item.getString("nickname");



                HashMap<String,String> hashMap = new HashMap<>();

<<<<<<< HEAD
                hashMap.put(TAG_ID, num.toString());
=======
                hashMap.put(TAG_ID, id);
>>>>>>> 401352adadd68d7c691c78439e1bb5d12e08edfa
                hashMap.put(TAG_NAME, name); //value
                hashMap.put(TAG_ADDRESS, address);
                shops.put(name, shop);

                mArrayList.add(hashMap);
                names.add(name);

            }

            ListAdapter adapter = new SimpleAdapter(
                    Chicken.this, mArrayList, R.layout.item_list_place,
                    new String[]{TAG_ID,TAG_NAME, TAG_ADDRESS},
                    new int[]{R.id.num, R.id.name, R.id.address}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
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
