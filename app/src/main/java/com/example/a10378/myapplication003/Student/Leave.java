package com.example.a10378.myapplication003.Student;
//请假
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.R;
import com.example.a10378.myapplication003.Info_DB.use_info;

import java.util.Calendar;

public class Leave extends AppCompatActivity {
    private use_info user;
private Intent intent1=null;
private MyDatabaseHelper dbhelper;
private EditText editText1=null;
private EditText editText2=null;
private EditText editText3=null;
private ImageView imageView=null;
private EditText editText=null;
private int flag=-1;
private int flag2=0;
private String location="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        editText1=findViewById(R.id.leave_starttime);
        editText2=findViewById(R.id.leave_endtime);
        editText3=findViewById(R.id.leave_edit);
         imageView=findViewById(R.id.leave_link);
        user=(use_info) getIntent().getSerializableExtra("user");
        //String City=getIntent().getStringExtra("location");
        Toast.makeText(Leave.this,user.getId_number(),Toast.LENGTH_LONG).show();
        Button bt2=findViewById(R.id.leave_back);
        editText=findViewById(R.id.leave_location);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leave.this, Student_Main.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imageView.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(Leave.this, Main2Activity.class);
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
                        Toast.makeText(Leave.this,location, Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());

                }
                cursor.close();
            }
        });
        //填写开始日期
      editText1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              flag=0;
              showDatepickDlg(flag);
          }
      });
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    flag=0;
                    showDatepickDlg(flag);
                }
            }
        });
        //填写结束日期
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                showDatepickDlg(flag);
            }
        });
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    flag=1;
                    showDatepickDlg(flag);
                }
            }
        });
        Button bt1=findViewById(R.id.leave_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                String s3=editText3.getText().toString();
               // use_info user=(use_info)getIntent().getSerializableExtra("user");

                user=(use_info) getIntent().getSerializableExtra("user");
               String id_number=user.getId_number();
                //Toast.makeText(Leave.this, id_number, Toast.LENGTH_LONG).show();
                SQLiteDatabase db=dbhelper.getWritableDatabase();//得到数据库对象，已有则不创建
                switch (view.getId()){
                    case R.id.leave_button:
                        if (s3.length()==0||s1.length()==0||s2.length()==0||location.length()==0)
                        {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(Leave.this);
                            dialog.setTitle("提示");
                            dialog.setMessage("输入或地址获取不正确！");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Leave.this, Leave.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                }
                            });

                            dialog.show();
                            break;
                            }

                        ContentValues values=new ContentValues();
                        values.put("id_number",id_number);
                        values.put("location",location);
                        values.put("start_time",s1);
                        values.put("end_time",s2);
                        values.put("cause",s3);
                        values.put("name",user.getName());
                       // dbhelper.insert(db,"leave",values);
                        dbhelper.update(db, "leave", values, "id_number=?", new String[]{user.getId_number()});
                        values.clear();
                        db.close();
                        Toast.makeText(Leave.this, "填写成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Leave.this, Student_Main.class);
                        intent.putExtra("user",user);
                        startActivity(intent);

                        break;
                        default:
                            break;
                }


            }
        });

    }
    //获取系统日历信息，并用日期框点击显示出来
    protected void showDatepickDlg(final int flag){
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(Leave.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (flag==0)
                {
                    i1=i1+1;
                    Leave.this.editText1.setText(i + "-" + i1 + "-" + i2 );
                }
              else if (flag==1)
                {i1=i1+1;
                Leave.this.editText2.setText(i + "-" + i1 + "-" + i2 );}

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        }

}
