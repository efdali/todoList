package com.efdalincesu.todolist.DBSqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION=1;
    private static final String DB_NAME="todolist";
    public static final String TABLE_NAME="todo";
    public static final String ID_COLUMN="todo_id";
    public static final String TITLE_COLUMN="todo_title";
    public static final String SUMMARY_COLUMN="todo_summary";
    public static final String DATENOW_COLUMN="todo_datenow";
    public static final String DATE_COLUMN="todo_date";
    public static final String REMINDER_COLUMN="todo_reminder";
    public static final String STATUS_COLUMN="todo_status";

    private String createTable="CREATE TABLE " + TABLE_NAME +
            "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_COLUMN + " TEXT, "+ SUMMARY_COLUMN + " TEXT, " +
            DATENOW_COLUMN + " TEXT, " + DATE_COLUMN + " TEXT, " + REMINDER_COLUMN + " TEXT, " + STATUS_COLUMN + " INTEGER )";

    private String deleteTable="DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(deleteTable);
        onCreate(db);
    }
}
