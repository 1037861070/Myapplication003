package com.example.a10378.myapplication003;
//修改个人信息
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

public class Modify_info extends AppCompatActivity {
private EditText editText1=null;
private EditText editText2=null;
private EditText editText3=null;
private EditText editText4=null;
private EditText editText5=null;
private TextView textView1=null;
private use_info user;
private Drawable drawable;
private MyDatabaseHelper dbhelper;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        editText1=findViewById(R.id.modify_name);//姓名
        editText2=findViewById(R.id.register_inputclass);//班级
        editText3=findViewById(R.id.modify_inputidnumber);//学号

        editText4=findViewById(R.id.modify_inputpsd);//密码
        editText5=findViewById(R.id.register_inputimage);//人脸图片地址

        textView1=findViewById(R.id.textView6);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        //获取传递过来的数据
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        user=(use_info) getIntent().getSerializableExtra("user");
        //Log.d("333333",user.getName()+user.getSign_number()+user.getClassname());
        //Toast.makeText(this,user.getClassname()+user.getId_number(),Toast.LENGTH_SHORT).show();
    Button btn1=findViewById(R.id.modifyinfo_button);
    //点击确定修改按钮
    btn1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s1=editText1.getText().toString();
            String s2=editText2.getText().toString();
            String s3=editText3.getText().toString();
            SQLiteDatabase db=dbhelper.getWritableDatabase();
            String s4=editText4.getText().toString();
            Log.d("333333",s3);
            switch (view.getId()){
                case R.id.modifyinfo_button:
                    if (checkall(s1,s2,s4))
                    {
                        ContentValues values=new ContentValues();
                        values.put("name",s1);
                        values.put("class",s2);
                        values.put("password",s4);
                        dbhelper.update(db, "user", values, "id_number=?", new String[]{s3});
                        values.clear();

                    }
                    break;
                default:
                    break;
            }

        }
    });
    Button btn2=findViewById(R.id.modifyinfo_back);
    btn2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(Modify_info.this,Modify_info.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
    });
        editText1.setText(user.getName());
        editText2.setText(user.getClassname());
        editText3.setText(user.getId_number());
    editText3.setEnabled(false);
        editText4.setText(user.getPassword());
    editText5.setEnabled(false);
        textView1.setText(String.valueOf(user.getSign_number()));

        //判断姓名输入是否规范

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
    }

    private boolean checkall(String s1,String s2,String s3){
        if(check_class(s2)&&check_password(s3)&&check_username(s1))
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(Modify_info.this);
            dialog.setTitle("提示");
            dialog.setMessage("修改成功！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent=new Intent(Modify_info.this,Student_Main.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            });

            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent=new Intent(Modify_info.this,Modify_info.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            });
            dialog.show();
            return true;
        }
        else
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(Modify_info.this);
            dialog.setTitle("提示");
            dialog.setMessage("输入有误！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent=new Intent(Modify_info.this,Modify_info.class);
                    intent.putExtra("user",user);
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

    private boolean check_password(String s){
        String ss="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s);
        if(matcher.matches())
            return true;
        else
            return false;
    }
}
