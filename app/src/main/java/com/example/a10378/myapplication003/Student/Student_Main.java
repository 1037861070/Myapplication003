package com.example.a10378.myapplication003.Student;
//学生主界面

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.R;
import com.example.a10378.myapplication003.Info_DB.use_info;

public class Student_Main extends AppCompatActivity {
    private MyDatabaseHelper dbhelper;
    private ImageView student2=null;
    private ImageView student3=null;
    private ImageView student4=null;
    private ImageView student5=null;
    private use_info user;
    private Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__main);
        student2=findViewById(R.id.student_2);
        student3=findViewById(R.id.student_3);
        student4=findViewById(R.id.student_4);
        student5=findViewById(R.id.student_5);
        //得到传递过来的对象参数，方便下一步操作
        dbhelper = new MyDatabaseHelper(this, "dbst.db", null, 2); //数据库建立并升级
        dbhelper.getWritableDatabase();   //创建数据库
        user = (use_info) getIntent().getSerializableExtra("user");
        //Toast.makeText(this,user.getId_number()+user.getPassword(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //封装学生信息

        Cursor cursor = db.rawQuery("select* from user where id_number =? ", new String[]{user.getId_number()});
        if (cursor.moveToFirst()) {
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setClassname(cursor.getString(cursor.getColumnIndex("class")));
            user.setSign_number(cursor.getInt(cursor.getColumnIndex("sign_number")));
            Log.e("signnumber",String.valueOf(user.getSign_number()));
        }
        cursor.close();
        TextView textView1 = findViewById(R.id.Leave_record);
        //请假操作
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Student_Main.this, Leave.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        student2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Student_Main.this, Leave.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        //修改个人信息操作
        TextView textView2 = findViewById(R.id.info_management);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.info_management:
                        Intent intent = new Intent(Student_Main.this, Modify_info.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

            }
        });
        student4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Main.this, Modify_info.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        //退出
        TextView textView4 = findViewById(R.id.exit);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Student_Main.this, MainActivity.class);
                startActivity(intent);
            }
        });
        student5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Main.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //跳转签到界面
        student3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Main.this, Sign.class);
                Toast.makeText(Student_Main.this, user.getId_number() + user.getPassword(), Toast.LENGTH_SHORT).show();
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        TextView t2 = findViewById(R.id.sign_record);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Student_Main.this, Sign.class);
                Toast.makeText(Student_Main.this, user.getId_number() + user.getPassword(), Toast.LENGTH_SHORT).show();
                intent.putExtra("user", user);
                startActivity(intent);
                /*
                SQLiteDatabase db=dbhelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("select* from leave where id_number =? ",new String[]{user.getId_number()});
                if (cursor.moveToFirst()) {
                    do {
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String location =cursor.getString(cursor.getColumnIndex("location"));
                        Toast.makeText(Student_Main.this,name+location, Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());

                }
                cursor.close();
                */

            }
        });
    }
}
