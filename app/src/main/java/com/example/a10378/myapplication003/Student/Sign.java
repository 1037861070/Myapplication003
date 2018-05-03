package com.example.a10378.myapplication003.Student;
//签到

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    private Analysis_response analysis_response = null;
    private Response response = new Response();
    private Face_Ways face_ways = null;
    private Button check_facebtn=null;
    String faceToken = "";
    private int type=-1;
    private Response response1=null;
    private int flag2=0;
    private String  faceToken1="";
private  int flag=0;
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
        check_facebtn = findViewById(R.id.detect_face);
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
                type=1;
                editText.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Sign.this, Main2Activity.class);
                intent.putExtra("type",type);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });


        //显示在文本中
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag2=1;
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from location where id_number =? and location_type=?",
                        new String[]{user.getId_number(),"1"});
                if (cursor.moveToFirst()) {
                    do {
                        location = cursor.getString(cursor.getColumnIndex("Province"))+
                                cursor.getString(cursor.getColumnIndex("City"))+
                                cursor.getString(cursor.getColumnIndex("District"))+
                                cursor.getString(cursor.getColumnIndex("Street"));
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
                check_facebtn.setVisibility(View.VISIBLE);
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

                                            if (response==null){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Sign.this);
                                                        dialog.setTitle("图片格式不符合");
                                                        dialog.setMessage("分辨率最小为48*48，最大为4096*4096，且不超过2M");
                                                        dialog.setCancelable(false);
                                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent intent = new Intent(Sign.this, Sign.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        dialog.show();
                                                    }
                                                });
                                            }
                                         else if (response.getStatus()!=200){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Sign.this);
                                                        dialog.setTitle("提示");
                                                        dialog.setMessage("并发数限制，请重新检测！");
                                                        dialog.setCancelable(false);
                                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        });
                                                        dialog.show();
                                                    }
                                                });
                                            }
                                        else {
                                            flag=2;
                                            analysis_response = new Analysis_response(response);
                                            faceToken = analysis_response.getFaceToken();
                                            Log.e("333333333333333333333333333333333333", analysis_response.getFaceToken());
                                            //绘制图片
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    flag=3;
                                                    int arr[] = analysis_response.face_rectangle();
                                                    Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                                    Canvas canvas = new Canvas(bitmap1);
                                                    Paint p = new Paint();
                                                    p.setColor(Color.RED);
                                                    p.setStyle(Paint.Style.STROKE);//不填充
                                                    p.setStrokeWidth(6);
                                                    //画出矩形人脸
                                                    canvas.drawRect(arr[1], arr[0], arr[2] + arr[1], arr[3] + arr[0], p);
                                                    picture.setImageBitmap(bitmap1);
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
        //确定签到
        Button bt1 = findViewById(R.id.sign_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_button:
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(Sign.this);
                        dialog.setTitle("提示");
                        if (flag2==1){
                            if (flag==3){
                                //进行人脸搜索，开启线程
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final AlertDialog.Builder dialog = new AlertDialog.Builder(Sign.this);
                                        dialog.setTitle("提示");
                                        response1=face_ways.SearchFacetorken(faceToken);
                                        analysis_response.setResponse(response1);
                                        final float confidence=analysis_response.getConfidence();

                                        Log.e("confidence:",String .valueOf(confidence));
                                        //String result = new String(response1.getContent());
                                       //faceToken1=new Analysis_response(response1).getFaceToken();
                                        //Log.e("response1",response1.toString());
                                        if (response1.getStatus() != 200 || response1 == null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Sign.this);
                                                    dialog.setTitle("提示");
                                                    dialog.setMessage("并发数限制，请重新签到！");
                                                    dialog.setCancelable(false);
                                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });
                                                    dialog.show();
                                                }
                                            });
                                        }else
                                        {
                                            //只有相似度大于90%以上才会验证通过
                                            if (confidence>90.0){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.setMessage("签到成功！"+"\n"+
                                                                "匹配正确率:"+String .valueOf(confidence)+"%");
                                                        dialog.setCancelable(false);
                                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                SQLiteDatabase db=dbhelper.getWritableDatabase();
                                                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                                                        Locale.getDefault());
                                                                String date=simpleDateFormat.format(new java.util.Date());
                                                                ContentValues values = new ContentValues();
                                                                ContentValues values2=new ContentValues();
                                                                values2.put("sign_number",user.getSign_number()+1);
                                                                values.put("face_token",faceToken);
                                                                values.put("name", user.getName());
                                                                values.put("status",1);//1为已签到，正常
                                                                values.put("sign_time",date);
                                                                values.put("id_number", user.getId_number());
                                                                values.put("location",location);
                                                                Log.e("faceToken",faceToken);
                                                                //插入表中
                                                                //db.insert("user",null,values);
                                                                dbhelper.insert(db, "sign", values);
                                                                dbhelper.update(db,"user",values2,"id_number=?",
                                                                        new String[]{user.getId_number()});
                                                                values2.clear();

                                                                values.clear();
                                                                Intent intent = new Intent(Sign.this, Student_Main.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        dialog.show();
                                                    }
                                                });
                                            }
                                            else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Sign.this);
                                                        dialog.setTitle("提示");
                                                        dialog.setMessage("签到失败！"+"\n"+
                                                                "匹配正确率:"+String .valueOf(confidence)+"%");
                                                        dialog.setCancelable(false);
                                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent intent = new Intent(Sign.this, Sign.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        dialog.show();
                                                    }
                                                });
                                            }

                                        }
                                    }
                                }).start();

                            }
                          else {
                                dialog.setMessage("操作有误！");
                                if (flag==1||flag==2){
                                    dialog.setMessage("请检测人脸！");
                                }
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Sign.this, Sign.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }

                        }
                        else {
                            dialog.setMessage("请获取位置信息！");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(Sign.this, Sign.class);
                                    startActivity(intent);
                                }
                            });
                            dialog.show();
                        }
                }

            }
        });
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
                   // Log.e("uri1111111111", imageuri.toString());
                    flag=1;
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
