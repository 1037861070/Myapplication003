package com.example.a10378.myapplication003.Info_DB;

/**
 * Created by Administrator on 2018/4/19.
 */

public class leave_info {
    private String id_number;
    private String start_time;
    private String end_time;
    private String location;
    private String cause;
    private String name;

    public leave_info(String id_number, String start_time,
                      String end_time, String location, String cause, String name) {
        this.id_number = id_number;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.cause = cause;
        this.name = name;
    }

    public leave_info(){}

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
