package com.example.uswteami;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    EditText review;
    Button set;
    Spinner spi;
    String sh;

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
                final String data1 = sh;
                final String data2 = review.getText().toString();
                final String data3 = d[0];

                try {
                    PHPRequest request = new PHPRequest("http://uswteami.dothome.co.kr/my/review/shop/androidApply.php");
                    String result = request.PhPtest(data1, data2, data3);
                    if(result.equals("1")){
                        Toast.makeText(getApplication(),"댓글 저장 완료",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplication(),line,Toast.LENGTH_SHORT).show();
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


}

