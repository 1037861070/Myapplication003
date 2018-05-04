package com.example.a10378.myapplication003.Teacher;
//老师主界面
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.R;
import com.example.a10378.myapplication003.Info_DB.use_info;
import com.example.a10378.myapplication003.Student.Leave;
import com.example.a10378.myapplication003.Student.Main2Activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Teacher_Main extends AppCompatActivity {
private use_info user;
private int flag=0;
private int type=0;
boolean aBoolean=false;
private String time;
    private String Province;
    private String City;
    private String District;
    private String Street;
    private MyDatabaseHelper dbhelper;
    private ImageView student2=null;
    private ImageView student3=null;
    private ImageView student4=null;
    private ImageView student5=null;
    private EditText editText=null;
    private String location="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        user=(use_info) getIntent().getSerializableExtra("user");
        student2=findViewById(R.id.student_2);
        student3=findViewById(R.id.student_3);
        student4=findViewById(R.id.student_4);
        student5=findViewById(R.id.student_5);
        editText=findViewById(R.id.teacher_position);
        TextView textView1=findViewById(R.id.textView3);
        TextView textView2=findViewById(R.id.textView5);
        TextView textView3=findViewById(R.id.textView2);
        TextView textView4=findViewById(R.id.textView);
        //显示教师位置，作为签到凭据
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!aBoolean){
                    type=2;
                    Intent intent = new Intent(Teacher_Main.this, Main2Activity.class);
                    intent.putExtra("type",type);
                    intent.putExtra("user",user);
                    aBoolean=true;
                    startActivity(intent);

                }
               else {
                    SQLiteDatabase db=dbhelper.getWritableDatabase();
                    //获取位置数据
                    Cursor cursor=db.rawQuery("select* from location where id_number =? and location_type=? ",new String[]{user.getId_number(),"2"});
                    if (cursor.moveToFirst()) {
                        do {
                            Province=cursor.getString(cursor.getColumnIndex("Province"));
                            City=cursor.getString(cursor.getColumnIndex("City"));
                            District=cursor.getString(cursor.getColumnIndex("District"));
                            Street=cursor.getString(cursor.getColumnIndex("Street"));
                            location=Province+City+District+Street;
                            editText.setText("当前位置:"+location);
                            Toast.makeText(Teacher_Main.this,location, Toast.LENGTH_SHORT).show();
                        }while (cursor.moveToNext());

                    }
                    cursor.close();

                }
            }
        });
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
