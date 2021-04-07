package com.thornton.dietlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

    public DBHelper(Context context)
    {
        super(context, "diet log", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE_DIETLOG = "CREATE TABLE dietlog (" + "_id integer primary key autoincrement, item text, amount real, cal real, pro real)";
        String CREATE_TABLE_DIETLIST = "CREATE TABLE dietlist (" + "_id integer primary key autoincrement, item text, amount real, cal real, pro real)";
        String CREATE_TABLE_PROGRESSLOG = "CREATE TABLE progresslog (" + "_id integer primary key autoincrement, date text, weight text, muscle text, fat text)";

        db.execSQL(CREATE_TABLE_DIETLOG);
        db.execSQL(CREATE_TABLE_DIETLIST);
        db.execSQL(CREATE_TABLE_PROGRESSLOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
