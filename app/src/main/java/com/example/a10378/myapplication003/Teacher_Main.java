package com.example.a10378.myapplication003;
//老师主界面
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Teacher_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main);
        TextView textView1=findViewById(R.id.textView5);
        //查看签到信息界面
        textView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_Main.this,Sign_info.class);
                startActivity(intent);
            }
        });

        //返回登录界面
        TextView textView=findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_Main.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
