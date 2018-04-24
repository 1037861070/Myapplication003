package com.example.a10378.myapplication003;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.use_info;
import com.example.a10378.myapplication003.Student.Find_Pswd;
import com.example.a10378.myapplication003.Student.Main2Activity;
import com.example.a10378.myapplication003.Student.Student_Main;
import com.example.a10378.myapplication003.Student.register;
import com.example.a10378.myapplication003.Teacher.Teacher_Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//初始登录
public class MainActivity extends AppCompatActivity {
private MyDatabaseHelper dbhelper;

private RadioButton radioButton1=null;
    private RadioButton radioButton2=null;
    private EditText editText1=null;
    private EditText editText2=null;
    private  int flag=-1;
    private Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        dbhelper.getWritableDatabase();   //创建数据库

        Button bt1=findViewById(R.id.signin_button);
        editText1=findViewById(R.id.username_edit);
        editText2=findViewById(R.id.password_edit);
        //判断学号是否符合规范
        editText1.addTextChangedListener(new TextWatcher() {
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

                if(matcher.matches()) {
                    drawable=getDrawable(R.drawable.icon1);
                    drawable.setBounds(0,0,75,75);
                    editText1.setError("right",drawable);

                }
                else{
                    drawable=getDrawable(R.drawable.icon2);
                    drawable.setBounds(0,0,75,75);
                    editText1.setError("error",drawable);}

            }
        });
        //判断密码是否输入规范
        editText2.addTextChangedListener(new TextWatcher() {
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
                    editText2.setError("right", drawable);
                    //Toast.makeText(MainActivity.this, "密码格式正确！", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    drawable=getDrawable(R.drawable.icon2);
                    drawable.setBounds(0,0,75,75);
                    editText2.setError("error",drawable);
                }
            }
        });
        //判断登录类型
        radioButton1=findViewById(R.id.radioButton);
        radioButton2=findViewById(R.id.radioButton2);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.radioButton:
                        flag=0;
                        radioButton2.setChecked(false);
                        radioButton1.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.radioButton2:
                        flag=1;
                        radioButton1.setChecked(false);
                        radioButton2.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        });
        //点击登录按钮操作
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbhelper.getWritableDatabase();//得到数据库对象，已有则不创建
                //获取输入框中值
                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                switch (view.getId()){
                    case R.id.signin_button:
                        if(checkall(s1,s2,flag)){
                            String s3=String.valueOf(flag);
                            Cursor cursor=db.rawQuery("select* from user where id_number =? and password=? and type=?"
                                    ,new String[]{s1,s2,s3});
                            if (!cursor.moveToFirst()) {
                                {
                                    Toast.makeText(MainActivity.this, "此用户不存在！请重新输入！", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                cursor.close();
                                break;
                            }
                            else
                            {
                                //将学号与密码传入，方便以后操作
                                use_info user=new use_info();
                                user.setId_number(s1);
                                user.setPassword(s2);
                                if (flag==0)
                                {
                                    Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Student_Main.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Teacher_Main.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        //点击注册事件
        TextView textView1=findViewById(R.id.register_link);
        textView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,register.class);
                startActivity(intent);
            }
        });
        //点击找回密码事件
        TextView textView2=findViewById(R.id.forget_link);
        textView2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Find_Pswd.class);
                startActivity(intent);
            }
        });
    }
    //整体检查是否输入符合规范
    private  boolean checkall(String s1,String s2,int flag)
    {
        if(check_idnumber(s1)&&check_password(s2)&&check_flag(flag))
        {
            return true;
        }
        else
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("输入有误");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.show();
            return  false;
        }
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
    private boolean check_password(String s){
        String ss="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s);
        if(matcher.matches())
            return true;
        else
            return false;
    }
    private boolean check_flag(int flag){
        if (flag==-1)
            return false;
        else
            return true;
    }
}
