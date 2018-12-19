package com.dev.aman.qrcodescanner.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dev.aman.qrcodescanner.model.QrModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "QRCODE.db";
    public static final String TABLE_NAME = "qrCode_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CONTENTS";
    public static final String COL_3 = "DATE";
    public static final String COL_4 = "TIME";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTENTS VARCHAR, DATE VARCHAR, TIME, VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String context, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,context);
        contentValues.put(COL_3,date);
        contentValues.put(COL_4,time);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<QrModel> getAllData(String sortBy) {

        ArrayList<QrModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(sortBy.equals("Size")){
            res = db.rawQuery("select * from "+TABLE_NAME+ " order by LENGTH(CONTENTS)",null);
        } else {
            res = db.rawQuery("select * from "+TABLE_NAME,null);
        }

        while(res.moveToNext()){
            list.add(new QrModel(
                    res.getString(res.getColumnIndex("ID")),
                    res.getString(res.getColumnIndex("CONTENTS")),
                    res.getString(res.getColumnIndex("DATE")),
                    res.getString(res.getColumnIndex("TIME")))
            );
        }

        return list;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
