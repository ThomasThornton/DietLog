package com.thornton.dietlog;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity
{
    DAO dao;
    Cursor items;
    SimpleCursorAdapter cursoradapter;
    EditText edittext_item, edittext_cal, edittext_pro;
    Button button_add, button_list;
    TextView textview_total_cal, textview_total_pro, textview_dietlog;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext_item = (EditText) findViewById(R.id.edittext_item);
        edittext_cal = (EditText) findViewById(R.id.edittext_cal);
        edittext_pro = (EditText) findViewById(R.id.edittext_pro);
        button_add = (Button) findViewById(R.id.button_add);
        button_list = (Button) findViewById(R.id.button_list);
        textview_total_cal = (TextView) findViewById(R.id.total_cal);
        textview_total_pro = (TextView) findViewById(R.id.total_pro);
        listview = (ListView) findViewById(android.R.id.list);
        textview_dietlog = (TextView) findViewById(R.id.text_dietlog);

        dao = new DAO(this);
        dao.open();

        items = dao.getLogItems();

        cursoradapter = new SimpleCursorAdapter(this, R.layout.dietitem, items, new String[]{"item", "amount", "cal", "pro"}, new int[]{R.id.textview_item, R.id.textview_amount, R.id.textview_cal, R.id.textview_pro}, 0);
        setListAdapter(cursoradapter);

        UpdateTotal();

        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    String item = edittext_item.getText().toString();

                    try
                    {
                        int cal = Integer.parseInt(edittext_cal.getText().toString());
                        int pro = Integer.parseInt(edittext_pro.getText().toString());

                        edittext_item.setText("Item");
                        edittext_cal.setText("Calories");
                        edittext_pro.setText("Protein");

                        dao.saveLogItem(item, 1, cal, pro);
                        dao.saveListItem(item, 1, cal, pro);

                        items = dao.getLogItems();
                        cursoradapter.changeCursor(items);

                        UpdateTotal();
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(MainActivity.this, "Wrong input", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        button_list.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DietListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        edittext_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && edittext_item.getText().toString().equals("Item"))
                    edittext_item.setText("");
            }
        });

        edittext_cal.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && edittext_cal.getText().toString().equals("Calories"))
                    edittext_cal.setText("");
            }
        });

        edittext_pro.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && edittext_pro.getText().toString().equals("Protein"))
                    edittext_pro.setText("");
            }
        });

        textview_dietlog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });
    }

    public void button_item_increment_click(View v)
    {
        LinearLayout linearlayout = (LinearLayout) v.getParent();

        TextView textview_item = (TextView) linearlayout.getChildAt(1);
        TextView textview_amount = (TextView) linearlayout.getChildAt(3);
        TextView textview_cal = (TextView) linearlayout.getChildAt(4);
        TextView textview_pro = (TextView) linearlayout.getChildAt(5);

        String item = textview_item.getText().toString();
        int amount = Integer.parseInt(textview_amount.getText().toString());
        int cal = Integer.parseInt(textview_cal.getText().toString());
        int pro = Integer.parseInt(textview_pro.getText().toString());

        dao.incrementItem(item, amount, cal, pro);

        items = dao.getLogItems();
        cursoradapter.changeCursor(items);

        UpdateTotal();
    }

    public void button_item_decrement_click(View v)
    {
        LinearLayout linearlayout = (LinearLayout) v.getParent();

        TextView textview_item = (TextView) linearlayout.getChildAt(1);
        TextView textview_amount = (TextView) linearlayout.getChildAt(3);
        TextView textview_cal = (TextView) linearlayout.getChildAt(4);
        TextView textview_pro = (TextView) linearlayout.getChildAt(5);

        String item = textview_item.getText().toString();
        int amount = Integer.parseInt(textview_amount.getText().toString());
        int cal = Integer.parseInt(textview_cal.getText().toString());
        int pro = Integer.parseInt(textview_pro.getText().toString());

        if (amount > 1)
            dao.decrementItem(item, amount, cal, pro);
        else
            dao.deleteLogItem(item);

        items = dao.getLogItems();
        cursoradapter.changeCursor(items);

        UpdateTotal();
    }

    protected void UpdateTotal()
    {
        int total_cal = 0;
        int total_pro = 0;

        if (items.getCount() > 0)
        {
            items.moveToFirst();
            do
            {
                total_cal += items.getInt(items.getColumnIndexOrThrow("cal"));
                total_pro += items.getInt(items.getColumnIndexOrThrow("pro"));
            }
            while (items.moveToNext());
        }

        textview_total_cal.setText(String.valueOf(total_cal) + " calories");
        textview_total_pro.setText(String.valueOf(total_pro) + " g  protein");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        String item;
        int cal, pro;
        Bundle bundle;

        if (resultCode == RESULT_OK)
        {
            bundle = intent.getExtras();

            item = bundle.getString("selected_item");
            cal = bundle.getInt("selected_cal");
            pro = bundle.getInt("selected_pro");

            dao.saveLogItem(item, 1, cal, pro);

            items = dao.getLogItems();
            cursoradapter.changeCursor(items);

            UpdateTotal();
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
