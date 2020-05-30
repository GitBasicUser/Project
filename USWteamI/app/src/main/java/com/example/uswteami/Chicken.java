package com.example.uswteami;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
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

public class Chicken extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextToSpeech myTTS;
    private ImageButton mVoiceBtn;

    private static String TAG = "phptest_MainActivity";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";
    private static String mJsonString;

    private TextView place_layout;
    ArrayList<HashMap<String, String>> mArrayList;
    HashMap<String,String> shops = new HashMap<>();
    HashMap<String,String> shopnames = new HashMap<>();
    List<String> names = new ArrayList<>();
    ListView mlistView;

    private static String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chicken_layout);

        place_layout = (TextView)findViewById(R.id.placeLayout);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();
        mVoiceBtn = (ImageButton)findViewById(R.id.voiceBtn);

        GetData task = new GetData();
        task.execute("http://uswteami.dothome.co.kr/my/board/chicken/json.php");

            myTTS = new TextToSpeech(this, new OnInitListener() {
                @Override
                public void onInit(int status) {
                    String Text = "치킨 카테고리입니다.메인메뉴로 돌아가시려면 뒤로 를 말해주세요.배달가능한 치킨집은";
                    myTTS.speak(Text, TextToSpeech.QUEUE_FLUSH, null);

                    for(String n : names){
                        myTTS.setSpeechRate(0.95f);
                        myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                    }

                    myTTS.setSpeechRate(1f);
                    String text2 = "입니다. 원하시는 가게이름 을 말해주세요.다시듣기는 다시, 메인메뉴로 돌아가기는 뒤로 입니다.";
                    myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                }
            });

            Intent get = getIntent();
            if(get.getStringExtra("flag_from_main").equals("y")) {
                place = get.getStringExtra("place");
                place_layout.setText(place);
            }

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

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Chicken.this,
                    "Please Wait", null, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d("a", "response  - " + result);

            if (result == null) {

            } else {

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

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            ArrayList<String> commant = new ArrayList<>();
            int k = 1;

            if(place.equals("리뷰 많은 순")) {
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    commant.add(item.getString("hit"));
                }
                for(int i = 0; i<commant.size(); i++){
                    for(int j = 0; j<commant.size() - 1; j++){
                        if(Integer.parseInt(commant.get(j)) <= Integer.parseInt(commant.get(j + 1))){
                            String tmp = commant.get(j);
                            commant.set(j, commant.get(j+1));
                            commant.set(j+1, tmp);
                        }
                    }
                }
            }

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                Integer num = i + 1;
                String name = item.getString("가게이름");
                String address = item.getString("주소");
                String shop = item.getString("nickname");
                String hit = item.getString("hit");
                Integer a = commant.size();
                Log.d("commnatSize: ", a.toString());

                if(place.equals("리뷰 많은 순")) {
                    if ( (commant.size() != 0) && (commant.get(0).equals(hit)) ) {
                        Integer numb = k;

                        boolean f = true;
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(TAG_ID, numb.toString());
                        hashMap.put(TAG_NAME, name);
                        hashMap.put(TAG_ADDRESS, address);

                        for(String n : names){
                            if(n.equals(name)) f = false;
                        }
                        if(f) {
                            mArrayList.add(hashMap);
                            names.add(name);
                            shops.put(name, shop);
                            shopnames.put(shop, name);

                            k++;
                            i = -1;
                            commant.remove(0);
                        }else continue;
                    }
                }else {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);

                    mArrayList.add(hashMap);
                    names.add(name);
                    shops.put(name, shop);
                    shopnames.put(shop, name);
                }
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


        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intent 음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text view 텍스트 보기로 설정
                    for(String n : names){
                        if(result.get(0).equals(n)){
                            Intent i = new Intent(Chicken.this,Menu.class);
                            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("shop", shops.get(n));
                            i.putExtra("shopname", shopnames.get(shops.get(n)));
                            i.putExtra("flag_from_chicken", "y");
                            startActivity(i);
                        }
                    }
                    if(result.get(0).equals("뒤로")){
                        Intent i = new Intent(Chicken.this, MainActivity.class);
                        i.putExtra("place", place);
                        startActivity(i);
                    }
                    else if(result.get(0).equals("다시") || result.get(0).equals("-")){
                        String Text = "치킨 카테고리입니다.배달가능한 치킨집은";
                        myTTS.speak(Text, TextToSpeech.QUEUE_FLUSH, null);

                        for(String n : names){
                            myTTS.speak(n, TextToSpeech.QUEUE_ADD, null);
                        }
                        String text2 = "입니다. 원하시는 가게이름을 말해주세요.";
                        myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);

                        String text3 = "다시듣기는 다시,  메인메뉴로 돌아가기는 뒤로 입니다.";
                        myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
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
