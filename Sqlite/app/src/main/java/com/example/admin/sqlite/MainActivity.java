package com.example.admin.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.example.admin.sqlite.Constants.TABLE_NAME;
import static com.example.admin.sqlite.Constants.TIME;
import static com.example.admin.sqlite.Constants.TITLE;
import static com.example.admin.sqlite.Constants._ID;


public class MainActivity extends AppCompatActivity {
    private EventsData events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                events = new EventsData(MainActivity.this);
                try{
                    addEvent();
                    Cursor cursor = getEvents();
                    showEvents(cursor);
                }finally{
                    events.close();
                }
            }
        });

        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                events = new EventsData(MainActivity.this);
                try{
                    editEvent();
                    Cursor cursor = getEvents();
                    showEvents(cursor);
                }finally{
                    events.close();
                }
            }
        });

        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                events = new EventsData(MainActivity.this);
                try{
                    deleteEvent();
                    Cursor cursor = getEvents();
                    showEvents(cursor);
                }finally{
                    events.close();
                }
            }
        });

        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                events = new EventsData(MainActivity.this);
                try{
                    resetAutoInc();
                    Cursor cursor = getEvents();
                    showEvents(cursor);
                }finally{
                    events.close();
                }
            }
        });

        final Button bt = (Button) findViewById(R.id.next);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private Cursor getEvents(){
        String[] FROM = {_ID,TIME,TITLE};
        String ORDER_BY = TIME + " DESC";
        SQLiteDatabase db = events.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,FROM,null,null,null,null,ORDER_BY);
        return cursor;
    }

    private void showEvents(Cursor cursor){
        StringBuilder builder = new StringBuilder("Saved events:\n");
        while(cursor.moveToNext()){
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            String title = cursor.getString(2);
            builder.append(id).append(" : ");
            builder.append(time).append(" : ");
            builder.append(title).append("\n");
        }
        TextView text1 = (TextView) findViewById(R.id.text);
        text1.setText(builder);
    }

    private long getLastId(){
        long id =0;
        SQLiteDatabase db = events.getWritableDatabase();
        String[] FROM = {_ID};
        String ORDER_BY = TIME + " DESC";
        Cursor cursor = db.query(TABLE_NAME,FROM,null,null,null,null,ORDER_BY,"1");
        while(cursor.moveToNext()){
            id = cursor.getLong(0);
        }
        return id;
    }




    private void addEvent() {
        EditText et1 = (EditText) findViewById(R.id.editText);
        String string = String.format("%1$s", et1.getText());
        SQLiteDatabase db = events.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(TITLE, string);
        db.insert(TABLE_NAME, null, values);
    }



    private void editEvent() {
        EditText et1 = (EditText) findViewById(R.id.editText);
        String string = String.format("%1$s", et1.getText());
        SQLiteDatabase db = events.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(TITLE, string);
        db.update(TABLE_NAME, values, "ROWID="+getLastId(), null);
    }



    private void deleteEvent() {
        SQLiteDatabase db = events.getWritableDatabase();
        db.delete(TABLE_NAME, "ROWID="+getLastId(), null);
    }



    private void resetAutoInc() {
        SQLiteDatabase db = events.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                TABLE_NAME + "'");
    }
}
