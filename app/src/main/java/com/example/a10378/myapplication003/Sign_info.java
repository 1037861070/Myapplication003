package com.example.a10378.myapplication003;
//签到记录
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Sign_info extends AppCompatActivity {
    private  String[] data={"Apple","banana","orange","Watermelon","pear","Grape","Cherry","mango"
    ,"Strawbery","Watermelo","Cherry"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_info);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Sign_info.this,android.R.layout.simple_dropdown_item_1line,data);
        ListView listView=findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}
