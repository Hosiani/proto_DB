package com.example.db_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import android.support.v7.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   // DB01 helper = null;

    private TextView textView;
    private EditText editTextKey, editTextValue;
    private DB01 helper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextKey = findViewById(R.id.edit_text_key);
        editTextValue = findViewById(R.id.edit_text_value);

        textView = findViewById(R.id.text_view);

        Button insertButton = findViewById(R.id.button_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (helper == null) {
                    helper = new DB01(getApplicationContext());
                }

                if (db == null) {
                    db = helper.getWritableDatabase();
                }

                String key = editTextKey.getText().toString();
                String value = editTextValue.getText().toString();

                insertData(db, key, Integer.valueOf(value));
            }
        });

        Button readButton = findViewById(R.id.button_read);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        Button deleteButton =  findViewById(R.id.deleteButton);
        // clickイベント追加
        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                deleteTable();
            }
        });
    }


    private void readData() {
        if (helper == null) {
            helper = new DB01(getApplicationContext());
        }

        if (db == null) {
            db = helper.getReadableDatabase();
        }
        Log.d("debug", "**********Cursor");

        Cursor cursor = db.query(
                "testdb",
                new String[]{"地名", "位置情報"},
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append(cursor.getString(0));
            sbuilder.append(": ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("\n");
            cursor.moveToNext();
        }

        // 忘れずに！
        cursor.close();

        Log.d("debug", "**********" + sbuilder.toString());
        textView.setText(sbuilder.toString());
    }

    private void insertData(SQLiteDatabase db, String com, int price) {

        ContentValues values = new ContentValues();
        values.put("company", com);
        values.put("stockprice", price);

        db.insert("testdb", null, values);
    }

private void deleteTable() {


    SQLiteDatabase db = helper.getWritableDatabase();
    try {
        db.execSQL("delete from testdb where stockprice = (select MIN(stockprice) from testdb) ");
    } finally {

    }
}
        }


