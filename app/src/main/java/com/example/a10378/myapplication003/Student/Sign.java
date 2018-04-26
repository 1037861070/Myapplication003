package com.example.a10378.myapplication003.Student;
//签到

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.use_info;
import com.example.a10378.myapplication003.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sign extends AppCompatActivity {
    private use_info user;
    private ImageView imageView1 = null;
    private EditText editText = null;
    private MyDatabaseHelper dbhelper;
    private String location = "";
    private ImageView imageView2=null;
    private ImageView imageView3=null;
    private ImageView picture=null;
    private Uri imageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        dbhelper = new MyDatabaseHelper(this, "dbst.db", null, 2); //数据库建立并升级
        user = (use_info) getIntent().getSerializableExtra("user");//获取用户信息
        Toast.makeText(Sign.this, user.getId_number() + user.getPassword(), Toast.LENGTH_SHORT).show();
        final TextView textView=findViewById(R.id.locationinfo);
        editText = findViewById(R.id.sign_location);
        picture=findViewById(R.id.picture);
        Button btn2 = findViewById(R.id.signback_button);
        //返回主界面
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign.this, Student_Main.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        final TextView textView1=findViewById(R.id.textView4);
        //找到位置信息
        imageView1 = findViewById(R.id.location);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Sign.this, Main2Activity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        //显示在文本中
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select* from leave where id_number =? ", new String[]{user.getId_number()});
                if (cursor.moveToFirst()) {
                    do {
                        location = cursor.getString(cursor.getColumnIndex("location"));
                        editText.setText("当前位置:" + location);
                        Toast.makeText(Sign.this, location, Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
        });
        imageView2=findViewById(R.id.leave_link);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                //添加权限
                if (ContextCompat.checkSelfPermission(Sign.this, Manifest.permission
                        .CAMERA)!= PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(
                        Sign.this, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Sign.this,new String[]
                            {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1
                    );
                }
                else
                {
                    take_photo();
                }
            }
        });

       //确定
        Button bt1 = findViewById(R.id.sign_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign.this, Student_Main.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
    //拍照，并存放在临时路径
    private  void  take_photo(){
        //创建图片缓存路径
        File OutputImage=new File(getExternalCacheDir(),"output_image.jpg");
        if (OutputImage.exists()){
            OutputImage.delete();
        }
        try {
            OutputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将文件转换为url对象
        if (Build.VERSION.SDK_INT>=24){
            imageuri= FileProvider.getUriForFile(Sign.this,
                    "com.example.a10378.myapplication003", OutputImage);
        }else {
            imageuri= Uri.fromFile(OutputImage);
        }
        //启动相机
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        Toast.makeText(Sign.this,imageuri.toString(),Toast.LENGTH_LONG).show();
        Log.d("111",imageuri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        startActivityForResult(intent,1);
    }
    //权限判断
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    take_photo();
                }else {
                    Toast.makeText(Sign.this,"权限不够！",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    //拍照时返回得结果参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                Log.d("222","ok");
                Toast.makeText(Sign.this,"success！",Toast.LENGTH_LONG).show();
                try {
                    Toast.makeText(Sign.this,"success！",Toast.LENGTH_LONG).show();
                    //Log.d("success",imageuri.toString());
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(imageuri));
                    picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
