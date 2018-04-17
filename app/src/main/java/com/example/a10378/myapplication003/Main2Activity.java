package com.example.a10378.myapplication003;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
public LocationClient locationClient;
private TextView posionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListenner());
        setContentView(R.layout.activity_main2);
        posionText=findViewById(R.id.position_text_view);
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()) {
        String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Main2Activity.this,permissions,1);
        }
        else {
            requestLocation();
        }
        }
    private void requestLocation(){
        locationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case 1:
               if(grantResults.length>0){
                   for (int result:grantResults){
                       if (result!=PackageManager.PERMISSION_GRANTED){
                           Toast.makeText(this,"必须同意权限",Toast.LENGTH_SHORT).show();
                           finish();
                           return;
                       }
                   }
                   requestLocation();
               }
               else {
                   Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                   finish();
               }
               break;
               default:
                   break;
       }
    }
public class MyLocationListenner implements BDLocationListener{
    @Override
    public void onReceiveLocation(final BDLocation bdLocation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder currenPosition =new StringBuilder();
                currenPosition.append("维度：").append(bdLocation.getLatitude()).append("\n");
                currenPosition.append("经度：").append(bdLocation.getLatitude()).append("\n");
                currenPosition.append("定位方式");
                if (bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                    currenPosition.append("GPS");
                }else if (bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                    currenPosition.append("网络");
                }
                posionText.setText(currenPosition);
            }
        });
    }

}

}

