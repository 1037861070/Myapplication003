package com.example.a10378.myapplication003.Teacher;
//老师主界面
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.R;
import com.example.a10378.myapplication003.Info_DB.use_info;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Teacher_Main extends AppCompatActivity {
private use_info user;
private int flag=0;

    private ImageView student2=null;
    private ImageView student3=null;
    private ImageView student4=null;
    private ImageView student5=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main);
       // user=(use_info) getIntent().getSerializableExtra("user");
        student2=findViewById(R.id.student_2);
        student3=findViewById(R.id.student_3);
        student4=findViewById(R.id.student_4);
        student5=findViewById(R.id.student_5);
        TextView textView1=findViewById(R.id.textView3);
        TextView textView2=findViewById(R.id.textView5);
        TextView textView3=findViewById(R.id.textView2);
        TextView textView4=findViewById(R.id.textView);
        //查看请假记录
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag=1;
                Intent intent=new Intent(Teacher_Main.this,show_info.class);
                //intent.putExtra("user",user);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        student2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                Intent intent=new Intent(Teacher_Main.this,show_info.class);
                //intent.putExtra("user",user);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        //查看签到信息界面
        textView2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                flag=2;
                Intent intent=new Intent(Teacher_Main.this,show_info.class);
               // intent.putExtra("user",user);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        student3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;
                Intent intent=new Intent(Teacher_Main.this,show_info.class);
                // intent.putExtra("user",user);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        //考勤信息统计
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=3;
                Intent intent=new Intent(Teacher_Main.this,show_info.class);
               // intent.putExtra("user",user);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        student4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=3;
                Intent intent=new Intent(Teacher_Main.this,show_info.class);
                // intent.putExtra("user",user);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
        //返回登录界面
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_Main.this,MainActivity.class);
                startActivity(intent);
            }
        });
        student5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Teacher_Main.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
