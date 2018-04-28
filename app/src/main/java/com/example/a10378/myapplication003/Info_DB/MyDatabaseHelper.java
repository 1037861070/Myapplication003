package com.example.a10378.myapplication003.Info_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 10378 on 2018/4/8.
 */
//数据库操作
public class MyDatabaseHelper extends SQLiteOpenHelper{
    //建立三个表，分别为user、leave、sign
    //private SQLiteDatabase db;

    public static final String user="CREATE TABLE \"user\" (\n" +
            "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`id_number`\tTEXT,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`face_token`\tTEXT,\n" +
            "\t`class`\tTEXT,\n" +
            "\t`type`\tINTEGER,\n" +
            "\t`sign_number`\tINTEGER\n" +
            ")";
    public static final String sign="CREATE TABLE \"sign\" (\n" +
            "\t`sign_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`id_number`\tTEXT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`status`\tINTEGER,\n" +
            "\t`sign_time`\tTEXT,\n" +
            "\t`location`\tTEXT\n" +
            ")";
    public  static final String leave="CREATE TABLE `leave` (\n" +
            "\t`leave_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`id_number`\tTEXT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`location`\tTEXT,\n" +
            "\t`start_time`\tTEXT,\n" +
            "\t`end_time`\tTEXT,\n" +
            "\t`cause`\tTEXT\n" +
            ")";
    private Context mcontext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mcontext=context;
    }

    @Override
    //执行数据库操作
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sign);
        db.execSQL(user);
        db.execSQL(leave);
        Toast.makeText(mcontext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    //更新数据库
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
     db.execSQL("drop table if exists user");
     db.execSQL("drop table if exists sign");
     db.execSQL("drop table if exists leave");
     onCreate(db);
    }
    //插入操作
    public void insert(SQLiteDatabase db, String table,ContentValues values){

        db = this.getWritableDatabase();
        db.insert(table,null, values);
        db.close();
    }
    //改
    public void update(SQLiteDatabase db, String table, ContentValues values, String wherecause, String[] whereArgs){
        db = this.getWritableDatabase();
        db.update(table, values, wherecause, whereArgs);
        db.close();
    }
    //查
    public Cursor query(SQLiteDatabase db, String table, String[] columns, String secletion,
                        String[] args, String groupBy, String having, String orderBy){
        db = this.getWritableDatabase();
        Cursor cursor = db.query(table, columns, secletion, args, groupBy, having, orderBy);
        //db.close();
        return cursor;
    }
    //删
    public void delete(SQLiteDatabase db, String table, String whereCause, String[] args){
        db = this.getWritableDatabase();
        db.delete(table, whereCause, args);
        db.close();
    }
}
