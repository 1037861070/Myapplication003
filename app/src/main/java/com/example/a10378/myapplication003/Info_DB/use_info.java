package com.example.a10378.myapplication003.Info_DB;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/11.
 */

public class use_info implements Serializable{
    private String name;
    private String classname;
    private String id_number;
    private String password;
    private int type;
    private int sign_number;
    private String face_token;

    public use_info(String name, String classname, String id_number, String password, int type, int sign_number) {
        this.name = name;
        this.classname = classname;
        this.id_number = id_number;
        this.password = password;
        this.type = type;
        this.sign_number = sign_number;
    }

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public use_info(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSign_number() {
        return sign_number;
    }

    public void setSign_number(int sign_number) {
        this.sign_number = sign_number;
    }

    @Override
    public String toString() {
        return "学号:"+getId_number()+"   姓名:"+getName()+"\n"+
                "班级:"+getClassname()+"\n"+
                "人脸标志:"+getFace_token()+"\n"+
                "签到次数:"+String.valueOf(getSign_number());
    }
}
