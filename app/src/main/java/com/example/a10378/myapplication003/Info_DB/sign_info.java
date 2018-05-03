package com.example.a10378.myapplication003.Info_DB;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/19.
 */

public class sign_info implements Serializable {
    private String id_number;
    private String name;
    private int status;
    private String sign_time;
    private String location;
    private String face_token;

    public sign_info(String id_number, String name, int status, String sign_time, String location) {
        this.id_number = id_number;
        this.name = name;
        this.status = status;
        this.sign_time = sign_time;
        this.location = location;
    }
    public sign_info(){}

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        String ss="签到信息不符合";
        if (getStatus()==1)
        {
            ss="已签到";
        }

        return "学号:"+getId_number()+"   姓名:"+getName()+"\n"+
                "地点:"+getLocation()+"\n"+
                "签到时间:"+getSign_time()+"\n"+
                 "人脸标志:"+getFace_token()+"\n"+
                "状态:"+ss;
    }
}
