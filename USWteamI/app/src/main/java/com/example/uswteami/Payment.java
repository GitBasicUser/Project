package com.example.uswteami;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Payment extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private ImageButton mVoiceBtn;
    private TextToSpeech myTTS;
    private TextView name;
    private TextView price;
    private TextView content;

    private ArrayList<String> pay_name = new ArrayList<>();
    private ArrayList<String> pay_price = new ArrayList<>();
    private ArrayList<String> pay_content = new ArrayList<>();
    private String menu_name;
    private String menu_price;
    private String menu_content;

    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();
    ListView payment;
    int f = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        Intent get = getIntent();
        menu_name = get.getStringExtra("name");

        mVoiceBtn = findViewById(R.id.voiceBtn);
        payment = (ListView)findViewById(R.id.payment);
        
        final MediaPlayer player_s = MediaPlayer.create(this, R.raw.start);

        showResult(menu_name);

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String text1 = menu_name + " 를 선택하셨 습니다.";
                myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);

                if(menu_name.equals("장바구니")){
                    if (pay_name == null) {
                        String t = "현재 장바구니에는";
                        myTTS.speak(t + "메뉴가 없습니다.주문 카테고리 이동은 뒤로 입니다.", TextToSpeech.QUEUE_ADD, null);
                    }else {
                        String text2 = "결제를 하시려면 결제, 메뉴확인은 확인 을 말해주세요.";
                        myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                    }
                }else {
                    String text2 = "장바구니에 저장을 원하시면 저장, ";
                    String text3 = "메뉴의 설명을 들으시려면 설 명 , 가격을 확인하시려면 가격 을 말해주세요.";
                    String text4 = "메뉴 카테고리 이동은 뒤로 입니다.";

                    myTTS.speak(text2, TextToSpeech.QUEUE_ADD, null);
                    myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
                    myTTS.speak(text4, TextToSpeech.QUEUE_ADD, null);
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

    private void showResult(String text){
        if(text.equals("장바구니")) {


            if(f == 0) {
                Intent g = getIntent();
                pay_name = (ArrayList<String>) g.getSerializableExtra("pay_name");
                pay_price = (ArrayList<String>) g.getSerializableExtra("pay_price");
                pay_content = (ArrayList<String>) g.getSerializableExtra("pay_content");
            }

            for (int i = 0; i < pay_name.size(); i++) {

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("pay_name", pay_name.get(i));
                hashMap.put("pay_price", pay_price.get(i));
                hashMap.put("pay_content", pay_content.get(i));

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(Payment.this, mArrayList, R.layout.item_list,
                    new String[]{"pay_name", "pay_price", "pay_content"},
                    new int[]{R.id.num, R.id.name, R.id.address});
            payment.setAdapter(adapter);

        }else{
            Intent get = getIntent();
            menu_price = get.getStringExtra("price");
            menu_content = get.getStringExtra("content");

            name = (TextView)findViewById(R.id.name);
            price = (TextView)findViewById(R.id.price);
            content = (TextView)findViewById(R.id.content);

            name.setText(menu_name);
            price.setText(menu_price);
            content.setText(menu_content);
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

                    String res = result.get(0).replace(" ", "");
                    int k = -1;

                    if(menu_name.equals("장바구니")){
                        if(res.equals("뒤로")){
                            if(f != 0){
                                Intent i = new Intent(Payment.this, Menu.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("flag_from_chicken", "no");
                                i.putExtra("name", "no");
                                i.putExtra("flag_delete", "y");
                                i.putExtra("pay_name", pay_name);
                                i.putExtra("pay_price", pay_price);
                                i.putExtra("pay_content", pay_content);
                                startActivity(i);
                            }else {
                                Intent i = new Intent(Payment.this, Menu.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("flag_from_chicken", "no");
                                i.putExtra("flag_delete", "n");
                                i.putExtra("name", "no");
                                startActivity(i);
                            }
                        }else if(res.equals("확인")){
                            int cnt = 0, pay = 0;
                            String text = "현재 장바구니에는";
                            if(pay_name.size() == 0){
                                myTTS.speak(text + "메뉴가 없습니다.주문 카테고리 이동은 뒤로 입니다.",TextToSpeech.QUEUE_ADD, null);
                            }else {
                                myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
                                for (String n : pay_name) {
                                    pay += Integer.parseInt(pay_price.get(cnt));
                                    myTTS.speak(n + " ", TextToSpeech.QUEUE_ADD, null);
                                    cnt++;
                                }
                                Integer c = cnt;
                                Integer p = pay;
                                myTTS.speak("의 " + c.toString() + " 개가 있으며, .", TextToSpeech.QUEUE_ADD, null);
                                myTTS.speak("총합 " + p.toString() + " 원 입니다.", TextToSpeech.QUEUE_ADD, null);
                                myTTS.speak("삭제할 메뉴가 있으시다면, 메뉴의 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                            }
                        }else if(res.equals("결재") || res.equals("결제")){
                            if(pay_name.size() == 0){
                                myTTS.speak("장바구니에 결제 할 메뉴가 없습니다.주문 카테고리 이동은 뒤로 입니다.",TextToSpeech.QUEUE_ADD, null);
                            }else {
                                int pay = 0;
                                int cnt = 0;
                                for (int i = 0; i < pay_price.size(); i++) {
                                    pay += Integer.parseInt(pay_price.get(i));
                                    cnt++;
                                }
                                Integer c = cnt;
                                Integer p = pay;
                                myTTS.speak(c.toString() + " 개의 메뉴 ", TextToSpeech.QUEUE_ADD, null);
                                myTTS.speak("총합 " + p.toString() + " 원 입니다.", TextToSpeech.QUEUE_ADD, null);
                                myTTS.speak("결제를 원하시면 결제승인, 장바구니 수정을 원하시면 수정 을 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                            }
                        }else if(res.equals("수정")){
                            String text = "현재 장바구니에는";
                            if(pay_name.size() == 0){
                                myTTS.speak(text + "메뉴가 없습니다.주문 카테고리 이동은 뒤로 입니다.",TextToSpeech.QUEUE_ADD, null);
                            }else {
                                myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
                                for (String n : pay_name) {
                                    myTTS.speak(n + " ", TextToSpeech.QUEUE_ADD, null);
                                }
                                myTTS.speak("가 있습니다.삭제를 원하시는 메뉴의 이름을 말해주세요.", TextToSpeech.QUEUE_ADD, null);
                            }
                        }else if(res.equals("결제승인") || res.equals("결재승인")){
                            myTTS.speak("결제가 정상적으로 완료 되었 습니다.", TextToSpeech.QUEUE_ADD, null);
                            //android.os.Process.killProcess(android.os.Process.myPid());
                            //System.exit(1);
                        }
                        for(String n : pay_name){
                            k++;
                            if(res.equals(n)) {
                                f++;
                                myTTS.speak(n + " 메뉴 가 장바구니 에서 삭제 되었습니다.", TextToSpeech.QUEUE_ADD, null);
                                pay_name.remove(k);
                                pay_content.remove(k);
                                pay_price.remove(k);
                                mArrayList.remove(k);
                                k = -1;
                                myTTS.speak("결제를 하시려면 결제, 치킨 카테고리 이동은 뒤로 입니다. ", TextToSpeech.QUEUE_ADD, null);
                                showResult("장바구니");
                                break;
                            }
                        }

                    }else {
                        if (res.equals("뒤로")) {
                            if(f != 0){
                                Intent i = new Intent(Payment.this, Menu.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("flag_from_chicken", "no");
                                i.putExtra("name", "no");
                                i.putExtra("flag_delete", "y");
                                i.putExtra("a", "aaa");
                                i.putExtra("pay_name", pay_name);
                                i.putExtra("pay_price", pay_price);
                                i.putExtra("pay_content", pay_content);
                                startActivity(i);
                            }else {
                                Intent i = new Intent(Payment.this, Menu.class);
                                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("flag_from_chicken", "no");
                                i.putExtra("flag_delete", "n");
                                i.putExtra("name", "no");
                                startActivity(i);
                            }
                        } else if (res.equals("저장")) {
                            //for(int i = 0; i<1; i++){
                                //myTTS.speak(menu_name + " 메뉴 가 장바구니에 저장 되었 습니다.", TextToSpeech.QUEUE_ADD, null);
                           // }
                            Intent i = new Intent(Payment.this, Menu.class);
                            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("flag_from_chicken", "no");
                            i.putExtra("flag_delete", "n");
                            i.putExtra("name", menu_name);
                            i.putExtra("price", menu_price);
                            i.putExtra("content", menu_content);
                            startActivity(i);
                        } else if (res.equals("설명")) {
                            String text = menu_name + " 메뉴 는  " + menu_content;
                            myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
                        } else if (res.equals("가격")) {
                            String text = menu_name + " 메뉴 의 가격 은    " + menu_price + "    원 입니다.";
                            myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
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
