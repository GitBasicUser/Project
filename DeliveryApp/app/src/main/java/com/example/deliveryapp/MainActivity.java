package com.example.deliveryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn_setting;
    private TextView test, text;
    private ArrayList<Button> bs = new ArrayList<>();

    private Intent intent;
    private SpeechRecognizer mRecognizer;
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private String place = " 별점순";
    private static Integer n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Log:", "0");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        test = (TextView)findViewById(R.id.testText);
        text = (TextView)findViewById(R.id.text);

        btn_setting = (Button)findViewById(R.id.btn_setting);
        bs.add(btn_setting);


        mRecognizer.startListening(intent);


        btn_setting.setOnClickListener(onClick);
        if(n != 0){
            Intent get = getIntent();
            place = get.getStringExtra("place");
            //Log.i("place: ", place);
            test.setText(place);
        }



    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_setting:
                    n++;
                    Intent i = new Intent(MainActivity.this, SettingActivity.class);
                    i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("place", place);
                    startActivity(i);
                    break;
            }
        }
    };

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) { }
        @Override
        public void onBeginningOfSpeech() { }
        @Override
        public void onRmsChanged(float v) { }
        @Override
        public void onBufferReceived(byte[] bytes) { }
        @Override
        public void onEndOfSpeech() {}
        @Override
        public void onError(int i) { }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            text.setText(rs[0]);

            btsClick(rs[0]);

            mRecognizer.startListening(intent);
        }

        @Override
        public void onPartialResults(Bundle bundle) { }
        @Override
        public void onEvent(int i, Bundle bundle) { }
    };

    public void btsClick(String text){
        if(text.equals(btn_setting.getText().toString())){
            n++;
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("place", place);
            startActivity(i);
        }
    }

}
