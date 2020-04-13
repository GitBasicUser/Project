package com.example.deliveryapp;

import android.app.Activity;
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

import androidx.annotation.Nullable;

public class SettingActivity extends Activity {

    private ArrayList<Button> bs = new ArrayList<>();
    private Button placeLayoutChange, back, save;
    private TextView placeLayout, text;
    private static String place, speech;

    private Intent intent;
    private SpeechRecognizer mRecognizer_setting;
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

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

        mRecognizer_setting = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer_setting.setRecognitionListener(recognitionListener);

        Intent getp = getIntent();
        place = getp.getStringExtra("place");

        placeLayoutChange = (Button)findViewById(R.id.placeLayoutChange);
        back = (Button)findViewById(R.id.back);
        save = (Button)findViewById(R.id.btn_save);
        bs.add(placeLayoutChange);
        bs.add(back);
        bs.add(save);

        text = (TextView)findViewById(R.id.text);
        placeLayout = (TextView)findViewById(R.id.placeLayout);
        placeLayout.setText(place);

        mRecognizer_setting.startListening(intent);

        back.setOnClickListener(onClick);
        save.setOnClickListener(onClick);
        placeLayoutChange.setOnClickListener(onClick);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.placeLayoutChange:
                    if(placeLayout.getText().toString().equals("거리순")) placeLayout.setText("별점순");
                    else placeLayout.setText("거리순");
                    break;
                case R.id.back:
                    Intent i = new Intent(SettingActivity.this, MainActivity.class);
                    i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("place", place);
                    startActivity(i);
                    break;
                case R.id.btn_save:
                    Intent i_save = new Intent(SettingActivity.this, MainActivity.class);
                    i_save.setFlags(i_save.FLAG_ACTIVITY_CLEAR_TOP);
                    i_save.putExtra("place", placeLayout.getText().toString());
                    Toast.makeText(SettingActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();

                    startActivity(i_save);
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
        public void onEndOfSpeech() { }
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

            mRecognizer_setting.startListening(intent);
        }

        @Override
        public void onPartialResults(Bundle bundle) { }
        @Override
        public void onEvent(int i, Bundle bundle) { }
    };

    public void btsClick(String text){
        if(text.equals(bs.get(0).getText().toString())){
            if(placeLayout.getText().toString().equals("거리순")) placeLayout.setText("별점순");
            else placeLayout.setText("거리순");
            mRecognizer_setting.startListening(intent);
        }else if(text.equals(bs.get(1).getText().toString())){
            Intent i = new Intent(SettingActivity.this, MainActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("place", place);
            startActivity(i);
        }else if(text.equals(bs.get(2).getText().toString())){
            Intent i_save = new Intent(SettingActivity.this, MainActivity.class);
            i_save.setFlags(i_save.FLAG_ACTIVITY_CLEAR_TOP);
            i_save.putExtra("place", placeLayout.getText().toString());
            Toast.makeText(SettingActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();

            startActivity(i_save);
        }
    }
}
