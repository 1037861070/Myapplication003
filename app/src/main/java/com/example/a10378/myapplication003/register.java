package com.example.a10378.myapplication003;
//注册用户
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
      MyDatabaseHelper dbhelper;
      private Drawable drawable;
     private  EditText editText1=null;
     private EditText editText2=null;
     private  EditText editText3=null;
     private EditText editText4=null;
     private EditText editText5=null;
     //private   SQLiteDatabase db;
     private EditText editText = null;
     private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2);
        Button bt1=findViewById(R.id.register_button);
        editText1=findViewById(R.id.register_inoutusername);
        editText2=findViewById(R.id.register_inputclass);
        editText3=findViewById(R.id.register_inputidnumber);
        editText4=findViewById(R.id.register_inputpsd);
        editText5=findViewById(R.id.register_input_aginpsd);
        //判断姓名是否输入符合规范
        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void afterTextChanged(Editable editable) {
                String ss="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
                Pattern pattern = Pattern.compile(ss);
                Matcher matcher = pattern.matcher(editable.toString());
                //int len=editText1.toString().length();
                if (matcher.matches()) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText1.setError("right", drawable);
                    //Toast.makeText(MainActivity.this, "密码格式正确！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    drawable=getDrawable(R.drawable.icon2);
                    drawable.setBounds(0,0,75,75);
                    editText1.setError("error",drawable);
                }
            }
        });
        //判断班级输入是否符合规范
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                int len=editable.toString().length();
                if(len>4&&len<15)
                   {
                        drawable = getDrawable(R.drawable.icon1);
                        drawable.setBounds(0, 0, 75, 75);
                        editText2.setError("right", drawable);

                    }
                    else
                    {
                        drawable=getDrawable(R.drawable.icon2);
                        drawable.setBounds(0,0,75,75);
                        editText2.setError("error",drawable);
                    }
            }
        });
        //判断学号输入是否规范
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String ss="^[0-9]{13}$";
                Pattern pattern = Pattern.compile(ss);
                Matcher matcher = pattern.matcher(editable.toString());
                //int len=editable.toString().length();
                if (matcher.matches()) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText3.setError("right", drawable);

                }
                else
                {
                    drawable=getDrawable(R.drawable.icon2);
                    drawable.setBounds(0,0,75,75);
                    editText3.setError("error",drawable);
                }
            }
        });
        //判断密码输入是否规范
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String ss="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,21}$";//字母数字6到15位
                Pattern pattern = Pattern.compile(ss);
                Matcher matcher = pattern.matcher(editable.toString());
                int len=editable.toString().length();

                if (matcher.matches()) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText4.setError("right", drawable);
                    //Toast.makeText(MainActivity.this, "密码格式正确！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    drawable=getDrawable(R.drawable.icon2);
                    drawable.setBounds(0,0,75,75);
                    editText4.setError("error",drawable);
                }
            }
        });
        //判断两次密码是否输入一致
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
              String s1=editText4.getText().toString();
              String s2=editText5.getText().toString();
              if (s1.equals(s2))
                  {
                      drawable = getDrawable(R.drawable.icon1);
                      drawable.setBounds(0, 0, 75, 75);
                      editText5.setError("right", drawable);

                  }
                  else
                  {
                      drawable=getDrawable(R.drawable.icon2);
                      drawable.setBounds(0,0,75,75);
                      editText5.setError("error",drawable);
                  }
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击注册按钮，将数据添加入数据库

                //用contentvalues对象重写put方法

                //获取输入数据
                SQLiteDatabase db=dbhelper.getWritableDatabase();//得到数据库对象，已有则不创建

                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                String s3=editText3.getText().toString();
                String s4=editText4.getText().toString();
                String s5=editText5.getText().toString();
                //判断输入条件
                switch (view.getId()){
                    case R.id.register_button:
                        //判断是否存在此用户
                        Cursor cursor=db.rawQuery("select* from user where id_number =?",new String[]{s3});
                        if (cursor.moveToFirst()) {
                            {
                                Toast.makeText(register.this, "已存在用戶！ 请重新输入！", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(register.this, register.class);
                                startActivity(intent);
                            }
                            cursor.close();
                            break;
                        }
                        if (checkall(s1,s2,s3,s4,s5))
                        {
                            ContentValues values=new ContentValues();
                            values.put("name",s1);
                            values.put("class",s2);
                            values.put("id_number",s3);
                            values.put("password",s4);
                            values.put("type",0);
                            values.put("sign_number",0);
                            //插入表中
                            //db.insert("user",null,values);
                            dbhelper.insert(db,"user",values);
                            values.clear();
                            db.close();
                            Toast.makeText(register.this, "注册成功！", Toast.LENGTH_LONG).show();
                        }
                    break;
                    default:
                        break;
                }
                //db.execSQL("insert into user(name,class,id_number,password) values ('?','?','?','?')",new String[]{s1,s2,s3,s4});
            }
        });
    }
    //整体判断是否符合情况
    private boolean checkall(String s1,String s2,String s3,String s4,String s5){
        if(check_class(s2)&&check_idnumber(s3)&&check_password(s4,s5)&&check_username(s1))
        {
        AlertDialog.Builder dialog=new AlertDialog.Builder(register.this);
        dialog.setTitle("提示");
        dialog.setMessage("注册成功！");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(register.this, register.class);
                startActivity(intent);
            }
        });
            dialog.show();
        return true;
        }
        else
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(register.this);
            dialog.setTitle("提示");
            dialog.setMessage("注册失败！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(register.this, register.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        return  false;
        }
    }
    private boolean check_username(String s){
        editText1=findViewById(R.id.register_inoutusername);
        String ss="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s);
       if(matcher.matches())
           return true;
       else
           return false;
    }
    private boolean check_class(String s){
      if(s.length()<4||s.length()>15)
          return false;
      else
          return true;
    }
    private boolean check_idnumber(String s){
        String ss="^[0-9]{13}$";
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches())
            return true;
        else
            return  false;
    }
    private boolean check_password(String s1,String s2){
        String ss="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s1);
        if(matcher.matches()&&s1.equals(s2))
            return true;
        else
            return false;
    }
}
