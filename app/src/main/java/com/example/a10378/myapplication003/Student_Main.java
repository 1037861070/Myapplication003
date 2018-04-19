package com.example.a10378.myapplication003;
//学生主界面
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

public class Student_Main extends AppCompatActivity {
    private MyDatabaseHelper dbhelper;

private use_info user;
    private Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__main);
        //得到传递过来的对象参数，方便下一步操作
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        dbhelper.getWritableDatabase();   //创建数据库
         user=(use_info) getIntent().getSerializableExtra("user");
        //Toast.makeText(this,user.getId_number()+user.getPassword(),Toast.LENGTH_SHORT).show();
        TextView textView1=findViewById(R.id.Leave_record);
        //请假操作
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Student_Main.this,Leave.class);
                intent.putExtra("user",user);

                startActivity(intent);
            }
        });
        //修改个人信息操作
        TextView textView2=findViewById(R.id.info_management);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db=dbhelper.getWritableDatabase();
                use_info user1=user;
                Log.d("11111",user1.getId_number()+user1.getPassword());

                switch (view.getId()){
                    case R.id.info_management:


                        Cursor cursor=db.rawQuery("select* from user where id_number =? ",new String[]{user.getId_number()});
                        if (cursor.moveToFirst()) {
                            user1.setName(cursor.getString(cursor.getColumnIndex("name")));
                            user1.setClassname(cursor.getString(cursor.getColumnIndex("class")));
                            user1.setSign_number(cursor.getInt(cursor.getColumnIndex("sign_number")));
                        }
                        cursor.close();
                        Log.d("22222",user1.getClassname()+user1.getSign_number());

                        Intent intent=new Intent(Student_Main.this,Modify_info.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                        break;
                        default:
                            break;
                }

            }
        });
        //退出
        TextView textView4=findViewById(R.id.exit);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Student_Main.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //签到
        TextView t2=findViewById(R.id.sign_record);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Student_Main.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
