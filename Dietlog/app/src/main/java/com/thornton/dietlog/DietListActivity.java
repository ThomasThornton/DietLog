package com.thornton.dietlog;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DietListActivity extends ListActivity
{
    DAO dao;
    Cursor items;
    SimpleCursorAdapter cursoradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_diet_list);

        dao = new DAO(this);
        dao.open();

        items = dao.getListItems();

        cursoradapter = new SimpleCursorAdapter(this, R.layout.dietlistitem, items, new String[]{"item", "cal", "pro"}, new int[]{R.id.textview_item, R.id.textview_cal, R.id.textview_pro}, 0);
        setListAdapter(cursoradapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        Cursor cursor = (Cursor) l.getItemAtPosition(position);

        Bundle bundle = new Bundle();

        bundle.putString("selected_item", cursor.getString(cursor.getColumnIndexOrThrow("item")));
        bundle.putInt("selected_cal", cursor.getInt(cursor.getColumnIndexOrThrow("cal")));
        bundle.putInt("selected_pro", cursor.getInt(cursor.getColumnIndexOrThrow("pro")));

        Intent intent = new Intent().putExtras(bundle);
        cursor.close();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void button_dietlistitem_remove_click(View v)
    {
        LinearLayout linearlayout = (LinearLayout) v.getParent();

        TextView textview_item = (TextView) linearlayout.getChildAt(0);
//        TextView textview_amount = (TextView) linearlayout.getChildAt(3);
//        TextView textview_cal = (TextView) linearlayout.getChildAt(4);
//        TextView textview_pro = (TextView) linearlayout.getChildAt(5);

        String item = textview_item.getText().toString();
//        int amount = Integer.parseInt(textview_amount.getText().toString());
//        int cal = Integer.parseInt(textview_cal.getText().toString());
//        int pro = Integer.parseInt(textview_pro.getText().toString());

//        if (amount > 1)
//            dao.decrementItem(item, amount, cal, pro);
//        else
            dao.deleteListItem(item);

        items = dao.getListItems();
        cursoradapter.changeCursor(items);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dao.close();
    }
}
