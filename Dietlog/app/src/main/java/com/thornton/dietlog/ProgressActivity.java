package com.thornton.dietlog;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ProgressActivity extends ListActivity
{
    DAO dao;
    Cursor items;
    SimpleCursorAdapter cursoradapter;
    EditText edittext_weight, edittext_muscle, edittext_fat;
    Button button_add;
    TextView textview_progresslog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        edittext_weight = (EditText) findViewById(R.id.edittext_weight);
        edittext_muscle = (EditText) findViewById(R.id.edittext_muscle);
        edittext_fat = (EditText) findViewById(R.id.edittext_fat);
        button_add = (Button) findViewById(R.id.button_add);
        textview_progresslog = (TextView) findViewById(R.id.text_progresslog);

        dao = new DAO(this);
        dao.open();

        items = dao.getProgressItems();

        cursoradapter = new SimpleCursorAdapter(this, R.layout.progressitem, items, new String[]{"date", "weight", "muscle", "fat"}, new int[]{R.id.textview_date, R.id.textview_weight, R.id.textview_muscle, R.id.textview_fat}, 0);
        setListAdapter(cursoradapter);

        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (edittext_weight.length() == 3 && edittext_muscle.length() == 3 && edittext_fat.length() == 3)
                {
                    Calendar calendar = Calendar.getInstance();
                    String date = String.valueOf(calendar.get(calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(calendar.MONTH) + 1);

                    try
                    {
                        Integer.parseInt(edittext_weight.getText().toString());
                        Integer.parseInt(edittext_muscle.getText().toString());
                        Integer.parseInt(edittext_fat.getText().toString());

                        String weight = edittext_weight.getText().toString();
                        String muscle = edittext_muscle.getText().toString();
                        String fat = edittext_fat.getText().toString();

                        weight = weight.substring(0, 2) + "," + weight.substring(2) + " kg";
                        muscle = muscle.substring(0, 2) + "," + muscle.substring(2) + " %";
                        fat = fat.substring(0, 2) + "," + fat.substring(2) + " %";

                        edittext_weight.setText("Weight");
                        edittext_muscle.setText("Muscle");
                        edittext_fat.setText("Fat");

                        dao.saveProgressItem(date, weight, muscle, fat);

                        items = dao.getProgressItems();
                        cursoradapter.changeCursor(items);
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(ProgressActivity.this, "Wrong input", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(ProgressActivity.this, "Wrong input", Toast.LENGTH_SHORT).show();
            }
        });

        edittext_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && edittext_weight.getText().toString().equals("Weight"))
                    edittext_weight.setText("");
            }
        });

        edittext_weight.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edittext_weight.length() == 3)
                    edittext_muscle.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edittext_muscle.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && edittext_muscle.getText().toString().equals("Muscle"))
                    edittext_muscle.setText("");
            }
        });

        edittext_muscle.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edittext_muscle.length() == 3)
                    edittext_fat.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edittext_fat.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && edittext_fat.getText().toString().equals("Fat"))
                    edittext_fat.setText("");
            }
        });

        textview_progresslog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void button_progressitem_remove_click(View v)
    {
        LinearLayout linearlayout = (LinearLayout) v.getParent();

        TextView textview_date = (TextView) linearlayout.getChildAt(0);
//        TextView textview_amount = (TextView) linearlayout.getChildAt(3);
//        TextView textview_cal = (TextView) linearlayout.getChildAt(4);
//        TextView textview_pro = (TextView) linearlayout.getChildAt(5);

        String date = textview_date.getText().toString();
//        int amount = Integer.parseInt(textview_amount.getText().toString());
//        int cal = Integer.parseInt(textview_cal.getText().toString());
//        int pro = Integer.parseInt(textview_pro.getText().toString());

//        if (amount > 1)
//            dao.decrementItem(item, amount, cal, pro);
//        else
        dao.deleteProgressItem(date);

        items = dao.getProgressItems();
        cursoradapter.changeCursor(items);
    }
}
