package com.example.a10378.myapplication003;
//签到记录
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Sign_info extends AppCompatActivity {
    private MyDatabaseHelper dbhelper;
    private use_info user;
    private int flag;
    private  String[] data={"Apple","banana","orange","Watermelon","pear","Grape","Cherry","mango"
    ,"Strawbery","Watermelo","Cherry"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_info);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        dbhelper.getWritableDatabase();   //创建数据库
        flag=getIntent().getIntExtra("flag",flag);
        Toast.makeText(this,String.valueOf(flag),Toast.LENGTH_SHORT).show();
        //user=(use_info) getIntent().getSerializableExtra("user");
        show_info(flag,dbhelper);
        ArrayAdapter<String> adapter=new ArrayAdapter
                <String>(Sign_info.this,android.R.layout.simple_dropdown_item_1line,data);
        ListView listView=findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
    private void show_info(int flag,MyDatabaseHelper dbhelper){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        //显示请假信息
        if (flag==1){

        }
        //显示考勤信息
        else if (flag==2){

        }
        //显示统计信息
        else if (flag==3){

        }
        else {
            Toast.makeText(this,"error！",Toast.LENGTH_SHORT);
            Intent intent=new Intent(Sign_info.this,Teacher_Main.class);
            startActivity(intent);
        }
    }
}
