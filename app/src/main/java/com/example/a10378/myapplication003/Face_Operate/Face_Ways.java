package com.example.a10378.myapplication003.Face_Operate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10378.myapplication003.R;
import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.FaceSetOperate;
import com.megvii.cloud.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/26.
 */

public class Face_Ways extends AppCompatActivity {
    private String key;
    private String secret;
    private String imageUrl;
    private StringBuffer sb = new StringBuffer();
    private CommonOperate commonOperate = null;
    private FaceSetOperate FaceSet = null;

    private Response response = null;

    //初始化数据
    public Face_Ways() {
        this.key = "mPE5MqlX2IE09V2sL1XqvU1C0WJpBrqb";//key
        this.secret = "-apimu0iqLh1x-BZzKM1rDbyUmCP2Wu-";//serect
        this.commonOperate = new CommonOperate(key, secret, false);//
        this.FaceSet = new FaceSetOperate(key, secret, false);//人脸集合
    }

    public Response Detect_face(byte[] arr) {
        //返回0，没有key或者secret
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(secret)) {
            return null;
        } else {
            //ArrayList<String> faces = new ArrayList<>();

            try {
                //检测第一个人脸，传的是本地图片文件
                //detect first face by local file
                //发送请求

                 response=commonOperate.detectByte(arr,1,null);

                Log.e("response",response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    //创建人脸库
    public Response CreatFaceset( String faces) {
       // String faceTokens = creatFaceTokens(faces);
        //ForceMerge中0为在faceset集合中不加入重复的facetoken,1为可以将图片加入相同的faceset集合中
        try {
            response = FaceSet.createFaceSet(null, "student_faces",
                    null, faces, null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //将faces集合里面的faceTokens做处理
    public String creatFaceTokens(ArrayList<String> faceTokens) {
        if (faceTokens == null || faceTokens.size() == 0) {
            return "";
        }
        StringBuffer face = new StringBuffer();
        for (int i = 0; i < faceTokens.size(); i++) {
            if (i == 0) {
                face.append(faceTokens.get(i));
            } else {
                face.append(",");
                face.append(faceTokens.get(i));
            }
        }
        return face.toString();
    }

    public Response addFaceset(String faceToken2) {

        //将图片加入指定的faceset集合
        Response response = null;
        try {
            response = FaceSet.addFaceByOuterId(faceToken2, "student_faces");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response SearchFacetorken(String faceToken2) {
        try {

            response = commonOperate.searchByOuterId(faceToken2, null,
                    null, "student_faces", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    //将本地res下的图片处理成byte数组形式
    /*private byte[] getBitmap(int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }*/


}
