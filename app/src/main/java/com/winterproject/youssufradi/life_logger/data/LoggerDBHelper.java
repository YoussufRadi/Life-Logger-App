package com.winterproject.youssufradi.life_logger.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.winterproject.youssufradi.life_logger.data.LoggerContract.LogEntry;
import com.winterproject.youssufradi.life_logger.data.LoggerContract.EventEntry;
/**
 * Created by y_sam on 12/1/2016.
 */

public class LoggerDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    static final String DATABASE_NAME = "movie.db";

    public LoggerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOGS_TABLE = "CREATE TABLE " + LogEntry.TABLE_NAME + " (" +
                LogEntry._ID + " INTEGER PRIMARY KEY," +
                LogEntry.COLUMN_HIGHLIGHT + " TEXT, " +
                LogEntry.COLUMN_LOCATION + " TEXT, " +
                LogEntry.COLUMN_DAY + " INTEGER NOT NULL, " +
                LogEntry.COLUMN_MONTH + " INTEGER NOT NULL, " +
                LogEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                LogEntry.COLUMN_PHOTOS + " TEXT " + " );";

        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +
                EventEntry._ID + " INTEGER PRIMARY KEY," +
                EventEntry.COLUMN_TITLE + " TEXT, " +
                EventEntry.COLUMN_DESCRIPTION + " TEXT, " +
                EventEntry.COLUMN_LOCATION + " TEXT, " +
                EventEntry.COLUMN_START_DAY + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_START_MONTH + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_START_YEAR + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_START_HOUR + " INTEGER, " +
                EventEntry.COLUMN_START_MINUTE + " INTEGER, " +
                EventEntry.COLUMN_END_DAY + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_END_MONTH + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_END_YEAR + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_END_HOUR + " INTEGER, " +
                EventEntry.COLUMN_END_MINUTE + " INTEGER, " +
                EventEntry.COLUMN_LOGS + " TEXT, " +
                EventEntry.COLUMN_PEOPLE_NAME + " TEXT, " +
                EventEntry.COLUMN_PEOPLE_NUMBER + " TEXT" + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_LOGS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LogEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
