package com.example.a10378.myapplication003.Student;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.a10378.myapplication003.Info_DB.MyDatabaseHelper;
import com.example.a10378.myapplication003.Info_DB.location_info;
import com.example.a10378.myapplication003.Info_DB.use_info;
import com.example.a10378.myapplication003.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
public LocationClient locationClient=null;
private MapView mapView;
private BaiduMap baiduMap;
private boolean isFirstlocate=true;
private TextView posionText;
private location_info Location;
private BDLocation bdLocation;
private MyDatabaseHelper dbhelper;
private use_info user;
private int flag=0;
private int type=-1;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=(use_info) getIntent().getSerializableExtra("user");
        type=getIntent().getIntExtra("type",0);
        dbhelper=new MyDatabaseHelper(this,"dbst.db",null,2);
        locationClient=new LocationClient(getApplicationContext());
        //Log.e("user information",user.getId_number());
        locationClient.registerLocationListener(myListener);//注册监听函数
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        mapView=findViewById(R.id.bmapview);//获取地图view实例
        baiduMap=mapView.getMap();//得到当前地图实例

        baiduMap.setMyLocationEnabled(true);//显示当前我的信息到地图上
        posionText=(TextView) findViewById(R.id.position_text_view);
        List<String> permissionList=new ArrayList<>();
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
        }
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
        Button btn=findViewById(R.id.baidumapbutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault());
                    String date=simpleDateFormat.format(new java.util.Date());//获取时间
                    SQLiteDatabase db=dbhelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("id_number",user.getId_number());
                    values.put("location_type",type);
                    values.put("Latitude",bdLocation.getLatitude());
                    values.put("Longitude",bdLocation.getLongitude());
                    values.put("Province",bdLocation.getProvince());
                    values.put("City",bdLocation.getCity());
                    values.put("District",bdLocation.getDistrict());
                    values.put("Street",bdLocation.getStreet());
                    values.put("location_time",date);
                   // Log.e("id_numner",user.getClassname());
                    StringBuilder currenPosition = new StringBuilder();
                    currenPosition.append("省：").append(bdLocation.getProvince()).append("\n");
                    currenPosition.append("市：").append(bdLocation.getCity()).append("\n");
                    currenPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
                    currenPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
                    Toast.makeText(Main2Activity.this, date+currenPosition.toString(),
                            Toast.LENGTH_LONG).show();
                    dbhelper.insert(db,"location",values);
                    values.clear();
                    db.close();
                    flag=1;

                }
               else {
                    Toast.makeText(Main2Activity.this, "已写入！请勿重复点击！",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        }
    private void navigateTo(BDLocation location){
        //显示百度地图位置
        if (isFirstlocate){
            bdLocation=location;
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstlocate=false;

        }
        MyLocationData.Builder locationBuild=new MyLocationData.Builder();
        locationBuild.latitude(location.getLatitude());//得到当前位置经度
        locationBuild.longitude(location.getLongitude());
        MyLocationData locationData=locationBuild.build();
        baiduMap.setMyLocationData(locationData);
         }

    private void requestLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setCoorType("bd09ll");
        //option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.setLocOption(option);
        locationClient.start();

    }


//权限判定是否符合条件
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

       }
    }
    //获取当前位置
public class MyLocationListener extends BDAbstractLocationListener{
    @Override

    public void onReceiveLocation(final BDLocation bdLocation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation ||
                        bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    navigateTo(bdLocation);

                }
                StringBuilder currenPosition = new StringBuilder();
                currenPosition.append("维度：").append(bdLocation.getLatitude()).append("\n");
                currenPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
                currenPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
                currenPosition.append("省：").append(bdLocation.getProvince()).append("\n");
                currenPosition.append("市：").append(bdLocation.getCity()).append("\n");
                currenPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
                currenPosition.append("街道：").append(bdLocation.getStreet()).append("\n");

                currenPosition.append("定位方式：");
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    currenPosition.append("GPS");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    currenPosition.append("网络");
                }
                Toast.makeText(Main2Activity.this,currenPosition.toString() , Toast.LENGTH_LONG).show();
                Log.d("描述：", String.valueOf(bdLocation.getLocType()) + "  "
                        + currenPosition.toString());
            }
        }).start();

    }


}

}

