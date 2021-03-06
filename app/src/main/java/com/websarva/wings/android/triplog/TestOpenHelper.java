package com.websarva.wings.android.triplog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "TripDB.db";
    private static final String TABLE_NAME = "tripdb";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_NAME_DATE = "date";
    private static final String COLUMN_NAME_LATITUDE= "latitude";
    private static final String COLUMN_NAME_LONGTITUDE = "longtitude";
    private static final String COLUMN_NAME_MEMO = "memo";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_DATE + " TEXT," +
                    COLUMN_NAME_LATITUDE + " REAL," +
                    COLUMN_NAME_LONGTITUDE + " REAL," +
                    COLUMN_NAME_MEMO + " TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
