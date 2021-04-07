package com.thornton.dietlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DAO
{
    private DBHelper helper;
    private SQLiteDatabase db;

    public DAO(Context context)
    {
        helper = new DBHelper(context);
    }

    void open()
    {
        db = helper.getWritableDatabase();
    }

    void close()
    {
        db.close();
    }

    Cursor getLogItems()
    {
        Cursor cursor = db.query("dietlog", new String[]{"_id", "item", "amount", "cal", "pro"}, null, null, null, null, "_id DESC");
        return cursor;
    }

    Cursor getListItems()
    {
        Cursor cursor = db.query("dietlist", new String[]{"_id", "item", "amount", "cal", "pro"}, null, null, null, null, "item ASC");
        return cursor;
    }

    Cursor getProgressItems()
    {
        Cursor cursor = db.query("progresslog", new String[]{"_id", "date", "weight", "muscle", "fat"}, null, null, null, null, "_id DESC");
        return cursor;
    }

    void saveLogItem(String item, int amount, int cal, int pro)
    {
        Cursor cursor = db.query("dietlog", new String[]{"item"}, "item=?", new String[]{item}, null, null, null);

        if (cursor.getCount() == 0)
        {
            ContentValues values = new ContentValues();
            values.put("item", item);
            values.put("amount", amount);
            values.put("cal", cal);
            values.put("pro", pro);
            db.insert("dietlog", null, values);
        }
    }

    void saveListItem(String item, int amount, int cal, int pro)
    {
        Cursor cursor = db.query("dietlist", new String[]{"item"}, "item=?", new String[]{item}, null, null, null);

        if (cursor.getCount() == 0)
        {
            ContentValues values = new ContentValues();
            values.put("item", item);
            values.put("amount", amount);
            values.put("cal", cal);
            values.put("pro", pro);
            db.insert("dietlist", null, values);
        }
    }

    void saveProgressItem(String date, String weight, String muscle, String fat)
    {
        Cursor cursor = db.query("progresslog", new String[]{"date"}, "date=?", new String[]{date}, null, null, null);

        if (cursor.getCount() == 0)
        {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("weight", weight);
            values.put("muscle", muscle);
            values.put("fat", fat);
            db.insert("progresslog", null, values);
        }
    }

    void incrementItem(String item, int amount, int cal, int pro)
    {
        ContentValues values = new ContentValues();
        values.put("amount", amount + 1);
        values.put("cal", cal / amount * (amount + 1));
        values.put("pro", pro / amount * (amount + 1));
        db.update("dietlog", values, "item=?", new String[]{ item });
    }

    void decrementItem(String item, int amount, int cal, int pro)
    {
        ContentValues values = new ContentValues();
        values.put("amount", amount - 1);
        values.put("cal", cal / amount * (amount - 1));
        values.put("pro", pro / amount * (amount - 1));
        db.update("dietlog", values, "item=?", new String[]{ item });
    }

    void deleteLogItem(String item)
    {
        db.delete("dietlog", "item=?", new String[]{item});
    }

    void deleteListItem(String item)
    {
        db.delete("dietlist", "item=?", new String[]{ item });
    }

    void deleteProgressItem(String date)
    {
        db.delete("progresslog", "date=?", new String[]{date});
    }

}
