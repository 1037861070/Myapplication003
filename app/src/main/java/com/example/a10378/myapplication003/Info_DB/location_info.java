package com.example.a10378.myapplication003.Info_DB;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/20.
 */

public class location_info implements Serializable {
    private double Latitude;//维度
    private double Longitude;//经度
    private String Country;//国家
    private String City;//城市
    private String Street;//街道
    private String StreetNumber;//街道号
    private int Loctype;//网络类型

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
