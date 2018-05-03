package com.example.a10378.myapplication003.Student;
//注册用户

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
import android.graphics.drawable.Drawable;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.Face_Operate.Analysis_response;
import com.example.a10378.myapplication003.Face_Operate.Face_Ways;
import com.example.a10378.myapplication003.MainActivity;
import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.R;
import com.megvii.cloud.http.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    MyDatabaseHelper dbhelper;
    private Drawable drawable;
    private EditText editText1 = null;
    private EditText editText2 = null;
    private EditText editText3 = null;
    private EditText editText4 = null;
    private EditText editText5 = null;
    //private   SQLiteDatabase db;
    private EditText editText = null;
    private ImageView imageView1 = null;
    private ImageView imageView2 = null;
    private Toast toast = null;
    private Uri imageuri;
    private Bitmap bitmap;
    private Face_Ways face_ways = null;
    private Analysis_response analysis_response = null;
    private Response response = new Response();
    private byte[] arr = null;
    private String face_token = null;
    private Button detectface = null;
    private int flag = 0;
    private Response response1 = new Response();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbhelper = new MyDatabaseHelper(this, "dbst.db", null, 2);
        Button bt1 = findViewById(R.id.register_button);
        editText1 = findViewById(R.id.register_inoutusername);
        editText2 = findViewById(R.id.register_inputclass);
        editText3 = findViewById(R.id.register_inputidnumber);
        editText4 = findViewById(R.id.register_inputpsd);
        editText5 = findViewById(R.id.register_input_aginpsd);
        imageView1 = findViewById(R.id.leave_link);
        imageView2 = findViewById(R.id.register_picture);
        detectface = findViewById(R.id.registerfacedetect);
        //检测人脸图片
        detectface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.registerfacedetect:
                        // Log.e("111111111111111111111111", face_token);
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
                                    if (response == null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(register.this);
                                                dialog.setTitle("图片格式不符合");
                                                dialog.setMessage("分辨率最小为48*48，最大为4096*4096，且不超过2M");
                                                dialog.setCancelable(false);
                                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(register.this, register.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                dialog.show();
                                    }
                                });
                                    }

                                        else if (response.getStatus() != 200 ) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(register.this);
                                                    dialog.setTitle("提示");
                                                    dialog.setMessage("并发数限制，请重新注检测！");
                                                    dialog.setCancelable(false);
                                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });
                                                    dialog.show();
                                                }
                                            });
                                        }  else {

                                            flag = 2;//有人脸数据
                                            analysis_response = new Analysis_response(response);
                                            Log.e("333333333333333333333333333333333333", analysis_response.getFaceToken());
                                            face_token = analysis_response.getFaceToken();
                                            //绘制图片
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    flag = 3;//有人脸
                                                    int arr[] = analysis_response.face_rectangle();
                                                    Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                                    Canvas canvas = new Canvas(bitmap1);
                                                    Paint p = new Paint();
                                                    p.setColor(Color.RED);
                                                    p.setStyle(Paint.Style.STROKE);//不填充
                                                    p.setStrokeWidth(6);
                                                    Log.e("4444444444444444444444444444444", String.valueOf(arr[0]));
                                                    //画出矩形人脸
                                                    canvas.drawRect(arr[1], arr[0], arr[2] + arr[1], arr[3] + arr[0], p);
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
        //拍照并显示
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setVisibility(View.GONE);
                detectface.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(register.this, Manifest.permission
                        .CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        register.this, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(register.this, new String[]
                            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
                    );
                } else {
                    take_photo();
                }
            }
        });
        //取消注册，返回主界面
        Button button = findViewById(R.id.register_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //判断姓名是否输入符合规范
        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void afterTextChanged(Editable editable) {
                String ss = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
                Pattern pattern = Pattern.compile(ss);
                Matcher matcher = pattern.matcher(editable.toString());
                //int len=editText1.toString().length();
                if (matcher.matches()) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText1.setError("right", drawable);
                    //Toast.makeText(MainActivity.this, "密码格式正确！", Toast.LENGTH_SHORT).show();
                } else {
                    drawable = getDrawable(R.drawable.icon2);
                    drawable.setBounds(0, 0, 75, 75);
                    editText1.setError("error", drawable);
                }
            }
        });
        //判断班级输入是否符合规范
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editable.toString().length();
                if (len > 4 && len < 15) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText2.setError("right", drawable);

                } else {
                    drawable = getDrawable(R.drawable.icon2);
                    drawable.setBounds(0, 0, 75, 75);
                    editText2.setError("error", drawable);
                }
            }
        });
        //判断学号输入是否规范
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ss = "^[0-9]{13}$";
                Pattern pattern = Pattern.compile(ss);
                Matcher matcher = pattern.matcher(editable.toString());
                //int len=editable.toString().length();
                if (matcher.matches()) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText3.setError("right", drawable);

                } else {
                    drawable = getDrawable(R.drawable.icon2);
                    drawable.setBounds(0, 0, 75, 75);
                    editText3.setError("error", drawable);
                }
            }
        });
        //判断密码输入是否规范
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ss = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,21}$";//字母数字6到15位
                Pattern pattern = Pattern.compile(ss);
                Matcher matcher = pattern.matcher(editable.toString());
                int len = editable.toString().length();

                if (matcher.matches()) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText4.setError("right", drawable);
                    //Toast.makeText(MainActivity.this, "密码格式正确！", Toast.LENGTH_SHORT).show();
                } else {
                    drawable = getDrawable(R.drawable.icon2);
                    drawable.setBounds(0, 0, 75, 75);
                    editText4.setError("error", drawable);
                }
            }
        });
        //判断两次密码是否输入一致
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s1 = editText4.getText().toString();
                String s2 = editText5.getText().toString();
                if (s1.equals(s2)) {
                    drawable = getDrawable(R.drawable.icon1);
                    drawable.setBounds(0, 0, 75, 75);
                    editText5.setError("right", drawable);

                } else {
                    drawable = getDrawable(R.drawable.icon2);
                    drawable.setBounds(0, 0, 75, 75);
                    editText5.setError("error", drawable);
                }
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击注册按钮，将数据添加入数据库
                //用contentvalues对象重写put方法
                //获取输入数据
                final SQLiteDatabase db = dbhelper.getWritableDatabase();//得到数据库对象，已有则不创建
                final String s1 = editText1.getText().toString();
                final String s2 = editText2.getText().toString();
                final String s3 = editText3.getText().toString();
                final String s4 = editText4.getText().toString();
                String s5 = editText5.getText().toString();
                //判断输入条件
                switch (view.getId()) {
                    case R.id.register_button:
                        //判断是否存在此用户
                        Cursor cursor = db.rawQuery("select* from user where id_number =?", new String[]{s3});
                        if (cursor.moveToFirst()) {
                            {
                                Toast.makeText(register.this, "已存在用戶！ 请重新输入！", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(register.this, register.class);
                                startActivity(intent);
                            }
                            cursor.close();
                            break;
                        }
                        if (checkall(s1, s2, s3, s4, s5)) {
                            //输入成功的条件下
                            AlertDialog.Builder dialog = new AlertDialog.Builder(register.this);
                            dialog.setTitle("提示");
                            if (flag == 3) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        response1 = face_ways.CreatFaceset(face_token);
                                        //加入人脸
                                        //Response response2=face_ways.addFaceset(face_token);
                                        String responseString1 = new String(response1.getContent());
                                        //String responseString2= new String(response2.getContent());
                                        Log.e("responseString1", responseString1);
                                        //Log.e("responseString2",responseString2);
                                        if (response1.getStatus() != 200 || response1 == null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(register.this);
                                                    dialog.setTitle("提示");
                                                    dialog.setMessage("并发数限制，请重新注册！");
                                                    dialog.setCancelable(false);
                                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });
                                                    dialog.show();
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(register.this);
                                                    dialog.setTitle("提示");
                                                    dialog.setMessage("注册成功！");
                                                    dialog.setCancelable(false);
                                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            ContentValues values = new ContentValues();
                                                            values.put("name", s1);
                                                            values.put("face_token",face_token);
                                                            values.put("class", s2);
                                                            values.put("id_number", s3);
                                                            values.put("password", s4);
                                                            values.put("type", 0);
                                                            values.put("sign_number", 0);
                                                            //插入表中
                                                            //db.insert("user",null,values);
                                                            dbhelper.insert(db, "user", values);
                                                            values.clear();
                                                            db.close();
                                                            Toast.makeText(register.this, "注册成功！", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(register.this, MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(register.this, register.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    dialog.show();   }
                                            });

                                        }
                                    }
                                }).start();

                            } else {
                                dialog.setMessage("条件不符合！");

                                if (flag == 1 || flag == 2) {
                                    dialog.setMessage("请检测人脸！");
                                }
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(register.this, register.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }

                        }
                        break;
                    default:
                        break;
                }
                //db.execSQL("insert into user(name,class,id_number,password) values ('?','?','?','?')",new String[]{s1,s2,s3,s4});
            }
        });
    }

    //整体判断是否符合情况
    private boolean checkall(String s1, String s2, String s3, String s4, String s5) {
        if (check_class(s2) && check_idnumber(s3) && check_password(s4, s5) && check_username(s1)) {
            return true;
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(register.this);
            dialog.setTitle("提示");
            dialog.setMessage("输入有误！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(register.this, register.class);
                    startActivity(intent);
                }
            });
            dialog.show();
            return false;
        }
    }

    private boolean check_username(String s) {
        editText1 = findViewById(R.id.register_inoutusername);
        String ss = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    private boolean check_class(String s) {
        if (s.length() < 4 || s.length() > 15)
            return false;
        else
            return true;
    }

    private boolean check_idnumber(String s) {
        String ss = "^[0-9]{13}$";
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    private boolean check_password(String s1, String s2) {
        String ss = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";//字母数字6到15位
        Pattern pattern = Pattern.compile(ss);
        Matcher matcher = pattern.matcher(s1);
        if (matcher.matches() && s1.equals(s2))
            return true;
        else
            return false;
    }

    //拍照，并存放在临时路径
    private void take_photo() {
        //创建图片缓存路径
        File OutputImage = new File(getExternalCacheDir(), "register.jpg");
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
            imageuri = FileProvider.getUriForFile(register.this,
                    "com.example.a10378.myapplication003", OutputImage);
        } else {
            imageuri = Uri.fromFile(OutputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Toast.makeText(register.this, imageuri.toString(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(register.this, "权限不够！", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(register.this, "success！", Toast.LENGTH_LONG).show();
                    //Log.d("success",imageuri.toString());
                    bitmap = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(imageuri));
                    Log.e("uri1111111111", imageuri.toString());
                    flag = 1;
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
