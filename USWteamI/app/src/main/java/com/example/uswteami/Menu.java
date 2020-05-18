package com.example.uswteami;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class Menu extends AppCompatActivity {

    private static String TAG = "phptest_MainActivity";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";
    private String shop;


    ArrayList<HashMap<String, String>> mArrayList_main;
    ArrayList<HashMap<String, String>> mArrayList_side;
    ArrayList<HashMap<String, String>> mArrayList_soda;
    ListView mlistView_main;
    ListView mlistView_side;
    ListView mlistView_soda;
    String mJsonString_main;
    String mJsonString_side;
    String mJsonString_soda;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        Intent get = getIntent();
        shop = get.getStringExtra("shop");


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

                Integer num = i + 1;
                String name = item.getString("메뉴이름");
                String address = item.getString("가격");
                String where = item.getString("shop");

                HashMap<String, String> hashMap = new HashMap<>();

                if(shop.equals(where)) {
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);


                    mArrayList_main.add(hashMap);
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

                Integer num = i + 1;
                String name = item.getString("메뉴이름");
                String address = item.getString("가격");
                String where = item.getString("shop");

                HashMap<String, String> hashMap = new HashMap<>();

                if(shop.equals(where)) {
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);


                    mArrayList_side.add(hashMap);
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

                Integer num = i + 1;
                String name = item.getString("메뉴이름");
                String address = item.getString("가격");
                String where = item.getString("shop");

                HashMap<String, String> hashMap = new HashMap<>();

                if(shop.equals(where)) {
                    hashMap.put(TAG_ID, num.toString());
                    hashMap.put(TAG_NAME, name);
                    hashMap.put(TAG_ADDRESS, address);


                    mArrayList_soda.add(hashMap);
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

}
