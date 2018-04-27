package com.example.a10378.myapplication003.Student;
//签到

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import com.example.a10378.myapplication003.Face_Operate.Analysis_response;
import com.example.a10378.myapplication003.Face_Operate.Face_Ways;
import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.use_info;
import com.example.a10378.myapplication003.R;
import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.FaceSetOperate;
import com.megvii.cloud.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sign extends AppCompatActivity {
    private use_info user;
    private ImageView imageView1 = null;
    private EditText editText = null;
    private MyDatabaseHelper dbhelper;
    private String location = "";
    private ImageView imageView2 = null;
    private ImageView imageView3 = null;
    private ImageView picture = null;
    private Uri imageuri;
    private byte[] arr = null;
    private Bitmap bitmap;
    private Analysis_response analysis_response=null;
    private Response response = new Response();
    private Face_Ways face_ways = null;
    String faceToken = "";
    String key = "mPE5MqlX2IE09V2sL1XqvU1C0WJpBrqb";//api_key
    String secret = "-apimu0iqLh1x-BZzKM1rDbyUmCP2Wu-";//api_secret
    String imageUrl = "http://pic1.hebei.com.cn/003/005/869/00300586905_449eedbb.jpg";
    StringBuffer sb = new StringBuffer();
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);


        dbhelper = new MyDatabaseHelper(this, "dbst.db", null, 2); //数据库建立并升级
        user = (use_info) getIntent().getSerializableExtra("user");//获取用户信息
        Toast.makeText(Sign.this, user.getId_number() + user.getPassword(), Toast.LENGTH_SHORT).show();
        final TextView textView = findViewById(R.id.locationinfo);
        editText = findViewById(R.id.sign_location);
        picture = findViewById(R.id.picture);
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
        final TextView textView1 = findViewById(R.id.textView4);
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
        imageView2 = findViewById(R.id.leave_link);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                //添加权限
                if (ContextCompat.checkSelfPermission(Sign.this, Manifest.permission
                        .CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        Sign.this, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Sign.this, new String[]
                            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
                    );
                } else {
                    take_photo();
                }
            }
        });
        final Button check_facebtn = findViewById(R.id.detect_face);


        check_facebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.detect_face:

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
                                arr = baos.toByteArray();
                                try {
                                    face_ways = new Face_Ways();
                                    //调用人脸检测代码
                                    response = face_ways.Detect_face(arr);
                                    if (response!=null){
                                        //faceToken = getFaceToken(response);
                                        if (response.getStatus()==403)
                                        {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Log.e("222222222222222222222222", faceToken);
                                                    Toast.makeText(Sign.this,"并发数限超过限制！请重新检测！" ,Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                        analysis_response=new Analysis_response(response);
                                        Log.e("333333333333333333333333333333333333", analysis_response.getFaceToken());
                                        //获取图片人脸坐标
                                        int arr[]=analysis_response.face_rectangle();
                                        Log.e("222222222222222222222222", String.valueOf(arr[2]));
                                        Canvas canvas=new Canvas();
                                        Paint p=new Paint();
                                        p.setColor(Color.RED);
                                        canvas.drawRect(arr[2],arr[1],arr[0],arr[3],p);
                                        faceToken=analysis_response.getFaceToken();


                                        Toast.makeText(Sign.this, faceToken, Toast.LENGTH_LONG).show();
                                    }
                                  else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("222222222222222222222222", faceToken);
                                                Toast.makeText(Sign.this,"图片格式不符合！" +
                                                        "分辨率最小为48*48，最大为4096*4096，且不超过2M",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();


                        break;
                    default:
                        break;
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

    //解析response对象，得到其中的face_token值
    private String getFaceToken(Response response) throws JSONException {
        if (response.getStatus() != 200) {
            return new String(response.getContent());
        }
        //将response转换为字符串
        String res = new String(response.getContent());
        Log.e("response", res);
        //解析json对象，取出json中faces数组中第一个位置名为face_token字段的值
        JSONObject json = new JSONObject(res);
        String faceToken = json.optJSONArray("faces").optJSONObject(0).optString("face_token");
        return faceToken;
    }

    //拍照，并存放在临时路径
    private void take_photo() {
        //创建图片缓存路径
        File OutputImage = new File(getExternalCacheDir(), "output_image.jpg");
        if (OutputImage.exists()) {
            OutputImage.delete();
        }
        try {
            OutputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将文件转换为url对象
        if (Build.VERSION.SDK_INT >= 24) {
            imageuri = FileProvider.getUriForFile(Sign.this,
                    "com.example.a10378.myapplication003", OutputImage);
        } else {
            imageuri = Uri.fromFile(OutputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Toast.makeText(Sign.this, imageuri.toString(), Toast.LENGTH_LONG).show();
        Log.d("111", imageuri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(intent, 1);
    }

    //权限判断
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    take_photo();
                } else {
                    Toast.makeText(Sign.this, "权限不够！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    //拍照时返回得结果参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                Log.d("222", "ok");
                Toast.makeText(Sign.this, "success！", Toast.LENGTH_LONG).show();
                try {
                    Toast.makeText(Sign.this, "success！", Toast.LENGTH_LONG).show();
                    //Log.d("success",imageuri.toString());

                    bitmap = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(imageuri));
                    Log.e("uri1111111111", imageuri.toString());
                    picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    //将本地res下的图片处理成byte数组形式
    private byte[] getBitmap(int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
   /* private byte[] getBytesFormfile(File file){
        if (file==null){
            return  null;
        }
        try {
            FileInputStream stream=new FileInputStream(file);
            ByteArrayOutputStream out=new ByteArrayOutputStream(1000);
            byte []b=new byte[1000];
            int n;
            while ((n=stream.read(b))!=-1){
                out.write(b,0,n);
                stream.close();
                out.close();
                return out.toByteArray();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }*/
}
