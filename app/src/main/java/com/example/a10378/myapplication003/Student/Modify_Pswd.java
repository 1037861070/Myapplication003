package com.example.a10378.myapplication003.Student;
//修改密码
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Modify_Pswd extends AppCompatActivity {
private EditText editText1=null;
private EditText editText2=null;
private EditText editText3=null;
private MyDatabaseHelper dbhelper;
private Drawable drawable;
    String id_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_pswd);
        id_number=getIntent().getStringExtra("id_number");
        Button bt1=findViewById(R.id.modify_button);
        Button btn2=findViewById(R.id.modify_back_button);
        editText1=findViewById(R.id.findpsdtext1);
        editText2=findViewById(R.id.findpsdtext2);
        editText3=findViewById(R.id.findpsdtext3);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        editText1.setText(id_number);
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
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Modify_Pswd.this, Find_Pswd.class);
               // intent.putExtra("id_number",id_number);
                startActivity(intent);
            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String s1=editText2.getText().toString();
                String s2=editText3.getText().toString();
                if (s1.equals(s2))
                {
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
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                String s3=editText3.getText().toString();
                SQLiteDatabase db=dbhelper.getWritableDatabase();//得到数据库对象，已有则不创建
                switch (view.getId())
                {
                    case R.id.modify_button:

                    if (checkall(s1,s2,s3)) {
                        Cursor cursor = db.rawQuery("select* from user where id_number =?", new String[]{s1});
                        //判断此用户是否存在
                        if (cursor.moveToFirst()) //存在的话
                        {
                            ContentValues values = new ContentValues();
                            values.put("password", s2);
                            dbhelper.update(db, "user", values, "id_number=?", new String[]{s1});
                            values.clear();
                            cursor.close();
                            Toast.makeText(Modify_Pswd.this, "修改成功！", Toast.LENGTH_SHORT).show();
                            cursor.close();
                            db.close();
                            Intent intent = new Intent(Modify_Pswd.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(Modify_Pswd.this);
                            dialog.setTitle("提示");
                            dialog.setMessage("不存在此用户！");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Modify_Pswd.this, Modify_Pswd.class);
                                    intent.putExtra("id_number",id_number);
                                    startActivity(intent);
                                }
                            });
                            dialog.show();
                        }
                        cursor.close();
                    }
                    break;
                    default:
                        break;
                }

            }
        });
    }
    private boolean checkall(String s1,String s2,String s3){
        if(check_idnumber(s1)&&check_password(s2,s3))
        {
            return true;
        }
        else
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(Modify_Pswd.this);
            dialog.setTitle("提示");
            dialog.setMessage("输入不合法！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Modify_Pswd.this, Modify_Pswd.class);
                    intent.putExtra("id_number",id_number);
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
