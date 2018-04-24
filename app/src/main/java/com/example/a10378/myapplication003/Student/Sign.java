package com.example.a10378.myapplication003.Student;
//签到
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.use_info;
import com.example.a10378.myapplication003.R;

public class Sign extends AppCompatActivity {
    private use_info user;
    private ImageView imageView1=null;
    private EditText editText=null;
    private MyDatabaseHelper dbhelper;
    private String location="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        user=(use_info) getIntent().getSerializableExtra("user");//获取用户信息
        Toast.makeText(Sign.this,user.getId_number()+user.getPassword(),Toast.LENGTH_SHORT).show();

        editText=findViewById(R.id.sign_location);
        Button btn2=findViewById(R.id.signback_button);
        //返回主界面
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign.this, Student_Main.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        imageView1=findViewById(R.id.location);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Sign.this, Main2Activity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbhelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("select* from leave where id_number =? ",new String[]{user.getId_number()});
                if (cursor.moveToFirst()) {
                    do {
                        location=cursor.getString(cursor.getColumnIndex("location"));
                        editText.setText("当前位置:"+location);
                        Toast.makeText(Sign.this,location, Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());

                }
                cursor.close();
            }
        });

        Button bt1=findViewById(R.id.sign_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Sign.this,Student_Main.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

    }
}
