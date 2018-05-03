package com.example.a10378.myapplication003.Teacher;
//签到记录
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.leave_info;
import com.example.a10378.myapplication003.Info_DB.sign_info;
import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.R;
import com.example.a10378.myapplication003.Info_DB.use_info;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class show_info extends AppCompatActivity {
    private MyDatabaseHelper dbhelper;

    private int flag;
    ListView listView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_info);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        dbhelper.getWritableDatabase();   //创建数据库
        flag=getIntent().getIntExtra("flag",flag);
         listView=findViewById(R.id.listview);
        //Toast.makeText(this,String.valueOf(flag),Toast.LENGTH_SHORT).show();
        //user=(use_info) getIntent().getSerializableExtra("user");
        show_info(flag,dbhelper);

    }
    private void show_info(int flag,MyDatabaseHelper dbhelper){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        //显示请假信息
        if (flag==1){

            ArrayAdapter<leave_info> adapter=new ArrayAdapter
                    <leave_info>(this,android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            Cursor cursor=db.query("leave",null,
                    null,null,null,null,null);
            if (cursor.moveToFirst()) {
              do{
                  leave_info leave=new leave_info();
                  leave.setName(cursor.getString((cursor.getColumnIndex("name"))));
                  leave.setId_number(cursor.getString((cursor.getColumnIndex("id_number"))));
                  leave.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                  leave.setStart_time(cursor.getString((cursor.getColumnIndex("start_time"))));
                  leave.setEnd_time(cursor.getString((cursor.getColumnIndex("end_time"))));
                  leave.setCause(cursor.getString((cursor.getColumnIndex("cause"))));
                  adapter.add(leave);
              }while (cursor.moveToNext());
          }
            cursor.close();
            //添加适配器，将查询到的数据添加在适配器中


        }
        //显示考勤信息
        else if (flag==2){

            ArrayAdapter<sign_info> adapter=new ArrayAdapter
                    <sign_info>(this,android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            Cursor cursor=db.query("sign",null,
                    null,null,null,null,null);
            if (cursor.moveToFirst()) {
                do{
                    sign_info sign=new sign_info();
                    sign.setName(cursor.getString((cursor.getColumnIndex("name"))));
                    sign.setId_number(cursor.getString((cursor.getColumnIndex("id_number"))));
                    sign.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                    sign.setSign_time(cursor.getString((cursor.getColumnIndex("sign_time"))));
                    sign.setStatus(cursor.getInt((cursor.getColumnIndex("status"))));
                    sign.setFace_token(cursor.getString(cursor.getColumnIndex("face_token")));
                    //leave.setCause(cursor.getString((cursor.getColumnIndex("cause"))));
                    adapter.add(sign);
                    Log.e("sign",sign.getFace_token());
                }while (cursor.moveToNext());
            }
            cursor.close();
            //添加适配器，将查询到的数据添加在适配器中

        }
        //显示统计信息
        else if (flag==3){
            ArrayAdapter<use_info> adapter=new ArrayAdapter
                    <use_info>(this,android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            Cursor cursor=db.query("user",null,
                    "type=?",new String[]{"0"},null,null,null);
            if (cursor.moveToFirst()) {
                do{
                    use_info user=new use_info();
                    user.setName(cursor.getString((cursor.getColumnIndex("name"))));
                    user.setId_number(cursor.getString((cursor.getColumnIndex("id_number"))));
                    user.setClassname(cursor.getString((cursor.getColumnIndex("class"))));
                    user.setSign_number(cursor.getInt((cursor.getColumnIndex("sign_number"))));
                    user.setFace_token(cursor.getString(cursor.getColumnIndex("face_token")));
                    //leave.setCause(cursor.getString((cursor.getColumnIndex("cause"))));
                    adapter.add(user);
                    Log.e("user",user.getFace_token());
                }while (cursor.moveToNext());
            }
            cursor.close();
            //添加适配器，将查询到的数据添加在适配器中
        }
        else {
            Toast.makeText(this,"error！",Toast.LENGTH_SHORT);
            Intent intent=new Intent(show_info.this,Teacher_Main.class);
            startActivity(intent);
        }
    }
}
