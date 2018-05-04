package com.example.a10378.myapplication003.Info_DB;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/20.
 */

public class location_info implements Serializable {
    private String id_number;//学号
    private int location_type;//类型
    private double Latitude;//维度
    private double Longitude;//经度
    private String Country;//国家
    private String City;//城市
    private String Street;//街道
    private String StreetNumber;//街道号
    private String location_time;//获取时间
    private int Loctype;//网络类型

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public int getLocation_type() {
        return location_type;
    }

    public void setLocation_type(int location_type) {
        this.location_type = location_type;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getStreetNumber() {
        return StreetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
    }

    public int getLoctype() {
        return Loctype;
    }

    public void setLoctype(int loctype) {
        Loctype = loctype;
    }
}
