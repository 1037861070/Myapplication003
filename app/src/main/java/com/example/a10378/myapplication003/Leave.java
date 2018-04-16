package com.example.a10378.myapplication003;
//请假
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Leave extends AppCompatActivity {
private Intent intent1=null;
private MyDatabaseHelper dbhelper;
private EditText editText1=null;
private EditText editText2=null;
private EditText editText3=null;
private int flag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2); //数据库建立并升级
        editText1=findViewById(R.id.leave_starttime);
        editText2=findViewById(R.id.leave_endtime);
        editText3=findViewById(R.id.leave_edit);
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
               intent1=getIntent();
               String id_number=intent1.getStringExtra("id_number");
                //Toast.makeText(Leave.this, id_number, Toast.LENGTH_LONG).show();
                SQLiteDatabase db=dbhelper.getWritableDatabase();//得到数据库对象，已有则不创建
                switch (view.getId()){
                    case R.id.leave_button:
                        if (s3.length()==0)
                        {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(Leave.this);
                            dialog.setTitle("提示");
                            dialog.setMessage("需输入缘由！");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Leave.this, Leave.class);
                                    startActivity(intent);
                                }
                            });

                            dialog.show();
                            break;
                            }

                        ContentValues values=new ContentValues();
                        values.put("id_number",id_number);
                        values.put("start_time",s1);
                        values.put("end_time",s2);
                        values.put("cause",s3);
                        dbhelper.insert(db,"leave",values);
                        values.clear();
                        db.close();
                        Toast.makeText(Leave.this, "填写成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Leave.this, Student_Main.class);
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
