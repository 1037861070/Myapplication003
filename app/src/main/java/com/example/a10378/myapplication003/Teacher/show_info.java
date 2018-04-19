package com.example.a10378.myapplication003.Teacher;
//签到记录
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.leave_info;
import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.R;
import com.example.a10378.myapplication003.Info_DB.use_info;

import java.util.ArrayList;
import java.util.List;

public class show_info extends AppCompatActivity {
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

    }
    private void show_info(int flag,MyDatabaseHelper dbhelper){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        //显示请假信息
        if (flag==1){
            List<leave_info> list=new ArrayList<>();
            Cursor cursor=db.query("leave",null,
                    null,null,null,null,null);
            if (cursor.moveToFirst()) {
              do{
                  leave_info leave=new leave_info();
                  leave.setName(cursor.getString((cursor.getColumnIndex("name"))));
                  leave.setId_number(cursor.getString((cursor.getColumnIndex("id_number"))));
                  leave.setLocation(cursor.getString((cursor.getColumnIndex("location"))));
                  leave.setStart_time(cursor.getString((cursor.getColumnIndex("start_time"))));
                  leave.setEnd_time(cursor.getString((cursor.getColumnIndex("end_time"))));
                  leave.setCause(cursor.getString((cursor.getColumnIndex("cause"))));
                  list.add(leave);
              }while (cursor.moveToNext());
          }
            cursor.close();
            //添加适配器，将查询到的数据添加在适配器中
            ArrayAdapter<String> adapter=new ArrayAdapter
                    <String>(show_info.this,android.R.layout.simple_dropdown_item_1line,data);
            ListView listView=findViewById(R.id.listview);
            listView.setAdapter(adapter);
        }
        //显示考勤信息
        else if (flag==2){

        }
        //显示统计信息
        else if (flag==3){

        }
        else {
            Toast.makeText(this,"error！",Toast.LENGTH_SHORT);
            Intent intent=new Intent(show_info.this,Teacher_Main.class);
            startActivity(intent);
        }
    }
}
