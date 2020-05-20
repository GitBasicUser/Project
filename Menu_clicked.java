package com.example.teamproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import org.w3c.dom.Text;

public class Menu_clicked extends Activity {
    private int img;
    private Button btn_save,btn_back,btn_clear,btn_total;
    private Button btn_5000;

    private TextView total;

    private int value;
    private int a;
    private int where=0;


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml과 연결
        setContentView(R.layout.menu_clicked);

        final Intent intent = new Intent(this,MainActivity.class);

        ImageView profile = (ImageView) findViewById(R.id.img_prof);
        TextView name = (TextView) findViewById(R.id.tv_name);
        TextView price = (TextView) findViewById(R.id.tv_price);

        img = Integer.parseInt(intent.getStringExtra("profile"));
        profile.setImageResource(img);
        name.setText(intent.getStringExtra("name"));
        price.setText(intent.getStringExtra("price"));
        //-------------------------------------------------------------------------------------
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        btn_5000=(Button)findViewById(R.id.btn_5000);

        total=(TextView) findViewById(R.id.tot);

        View.OnClickListener cl=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==btn_save){
                    a=Integer.valueOf(total.getText().toString().trim());
                    total.setText("");
                    where=1;
                }
                else if(v==btn_5000){
                    total.setText(total.getText().toString()+5000);
                }
                else if(v==btn_total){
                    if(where==1){
                        a=a+Integer.valueOf(total.getText().toString().trim());
                        total.setText(Integer.toString(a));
                    }
                }
                else if(v==btn_clear){
                    total.setText("");
                }
              else if(v==btn_back){
                  startActivity(intent);
                }


            }
        };

        btn_save.setOnClickListener(cl);
        btn_clear.setOnClickListener(cl);
        btn_total.setOnClickListener(cl);
        btn_back.setOnClickListener(cl);



        //--------------------------------------------------------------------------------------
    }
}


