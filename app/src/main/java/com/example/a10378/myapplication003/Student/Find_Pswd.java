package com.example.a10378.myapplication003.Student;
//找回密码中间界面
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a10378.myapplication003.R;

public class Find_Pswd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__pswd);

        TextView textView1=findViewById(R.id.findpasd_button);
        TextView textView2=findViewById(R.id.findpasdback_button);
        textView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Find_Pswd.this,Modify_Pswd.class);
                startActivity(intent);
            }
        });

    }
}
