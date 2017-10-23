package com.example.admin.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.admin.sqlite.Constants.TABLE_NAME;
import static com.example.admin.sqlite.Constants._ID;
import static com.example.admin.sqlite.Constants.TITLE;
import static com.example.admin.sqlite.Constants.TIME;


/**
 * Created by Admin on 19/10/2560.
 */

public class EventsData extends SQLiteOpenHelper {

    public EventsData(Context ctx){super(ctx,"events.db",null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIME + " INTEGER, " +
                TITLE + " TEXT NOT NULL);"
        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
}
