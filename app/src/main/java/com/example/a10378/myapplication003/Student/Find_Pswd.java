package com.example.a10378.myapplication003.Student;
//找回密码中间界面
import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Face_Operate.Analysis_response;
import com.example.a10378.myapplication003.Face_Operate.Face_Ways;
import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.R;
import com.megvii.cloud.http.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Find_Pswd extends AppCompatActivity {
private ImageView imageView1=null;
private ImageView imageView2=null;
private TextView textView=null;
private Uri imageuri;
private Bitmap bitmap;
private Face_Ways face_ways=null;
private Analysis_response analysis_response=null;
private Response response=new Response();
private byte[] arr = null;
private String face_token=null;
private int flag=-1;
    private Response response1=null;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__pswd);
        //实时获取相机拍照
        imageView1=findViewById(R.id.leave_link);
        imageView2=findViewById(R.id.picure_find_pswd);

        textView=findViewById(R.id.textView4);
        final Button btn3=findViewById(R.id.check_face);
        //检测人脸
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.check_face:
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
                                    Log.e("reponse",response.toString());
                                    if (response==null){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(Find_Pswd.this);
                                                dialog.setTitle("图片格式不符合");
                                                dialog.setMessage("分辨率最小为48*48，最大为4096*4096，且不超过2M");
                                                dialog.setCancelable(false);
                                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(Find_Pswd.this, Find_Pswd.class);
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
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(Find_Pswd.this);
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
                                            flag=2;//有人脸数据
                                            analysis_response=new Analysis_response(response);
                                            face_token=analysis_response.getFaceToken();
                                            Log.e("333333333333333333333333333333333333", analysis_response.getFaceToken());
                                            //绘制图片
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    flag=3;//有人脸
                                                    int arr[]=analysis_response.face_rectangle();
                                                    Bitmap bitmap1=bitmap.copy(Bitmap.Config.ARGB_8888,true);
                                                    Canvas canvas=new Canvas(bitmap1);
                                                    Paint p=new Paint();
                                                    p.setColor(Color.RED);
                                                    p.setStyle(Paint.Style.STROKE);//不填充
                                                    p.setStrokeWidth(6);
                                                    Log.e("4444444444444444444444444444444", String.valueOf(arr[0]));
                                                    //画出矩形人脸
                                                    canvas.drawRect(arr[1],arr[0],arr[2]+arr[1],arr[3]+arr[0],p);
                                                    imageView2.setImageBitmap(bitmap1);
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
        //点击拍照+号
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;//有拍照
                imageView1.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                btn3.setVisibility(View.VISIBLE);
                //添加权限
                if (ContextCompat.checkSelfPermission(Find_Pswd.this, Manifest.permission
                        .CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        Find_Pswd.this, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Find_Pswd.this, new String[]
                            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
                    );
                } else {
                    take_photo();
                }
            }
        });

        Button textView1=findViewById(R.id.findpasd_button);
        Button textView2=findViewById(R.id.findpasdback_button);
        //返回
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Find_Pswd.this, Find_Pswd.class);
                startActivity(intent);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Find_Pswd.this);
                dialog.setTitle("提示");
                if (flag==3){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            response1=face_ways.SearchFacetorken(face_token);
                            //获取置信度
                            analysis_response.setResponse(response1);
                            final float confidence=analysis_response.getConfidence();

                            Log.e("confidence:",String .valueOf(confidence));
                            if (response1.getStatus() != 200 || response1 == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Find_Pswd.this);
                                        dialog.setTitle("提示");
                                        dialog.setMessage("并发数限制，请重新验证！");
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
                                if (confidence>90.0){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(Find_Pswd.this);
                                            dialog.setTitle("提示");
                                            dialog.setMessage("验证成功！"+"\n"+
                                                    "匹配正确率:"+String .valueOf(confidence)+"%");
                                            dialog.setCancelable(false);
                                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(Find_Pswd.this, Modify_Pswd.class);
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
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(Find_Pswd.this);
                                            dialog.setTitle("提示");
                                            dialog.setMessage("验证失败！"+"\n"+
                                                    "匹配正确率:"+String .valueOf(confidence)+"%");
                                            dialog.setCancelable(false);
                                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(Find_Pswd.this, Find_Pswd.class);
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
                    dialog.setMessage("条件不符合！");
                    if (flag==1){
                        dialog.setMessage("无图片！");
                    }
                    else if (flag==2)
                    {
                        dialog.setMessage("请检测人脸！");
                    }

                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Find_Pswd.this, Find_Pswd.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
    //拍照，并存放在临时路径
    private void take_photo() {
        //创建图片缓存路径
        File OutputImage = new File(getExternalCacheDir(), "find_psw.jpg");
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
            imageuri = FileProvider.getUriForFile(Find_Pswd.this,
                    "com.example.a10378.myapplication003", OutputImage);
        } else {
            imageuri = Uri.fromFile(OutputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Toast.makeText(Find_Pswd.this, imageuri.toString(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Find_Pswd.this, "权限不够！", Toast.LENGTH_LONG).show();
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

                try {
                    Toast.makeText(Find_Pswd.this, "success！", Toast.LENGTH_LONG).show();
                    //Log.d("success",imageuri.toString());
                    bitmap = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(imageuri));
                    //Log.e("uri1111111111", imageuri.toString());
                    imageView2.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
