package com.example.a10378.myapplication003.Face_Operate;

import android.util.Log;

import com.megvii.cloud.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/4/26.
 */

public class Analysis_response {
    private Response response=null;
    private String faceToken=null;
    public Analysis_response(Response response){
        this.response=response;
    }
    //获取faceToken
    public String getFaceToken() {
        if (response.getStatus() != 200) {
            return new String(response.getContent());
        }
        //将response转换为字符串
        String res = new String(response.getContent());
        Log.e("response", res);
        //解析json对象，取出json中faces数组中第一个位置名为face_token字段的值
        JSONObject json = null;
        try {
            json = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        faceToken = json.optJSONArray("faces").optJSONObject(0).optString("face_token");
        return faceToken;
    }
    public int[] face_rectangle(){
        int arr[]=new int[4];
        //成功情况下
        if (response.getStatus()== 200) {
            String res = new String(response.getContent());
            JSONObject json = null;
            try {
                json=new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //分别获取图片人脸的宽度、纵坐标、横坐标、高度
            arr[0]=json.optJSONArray("faces").optJSONObject(0).//宽度
                    optJSONObject("face_rectangle").optInt("width");
            arr[1]=json.optJSONArray("faces").optJSONObject(0).//纵坐标
                    optJSONObject("face_rectangle").optInt("top");
            arr[2]=json.optJSONArray("faces").optJSONObject(0).//横坐标
                    optJSONObject("face_rectangle").optInt("left");
            arr[3]=json.optJSONArray("faces").optJSONObject(0).//高度
                    optJSONObject("face_rectangle").optInt("height");
            Log.e("arr[0]",String.valueOf(arr[0])) ;
            return arr;
        }
        else
            return null;
    }
}
