package com.winterproject.youssufradi.life_logger.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.winterproject.youssufradi.life_logger.data.LoggerContract.LogEntry;
/**
 * Created by y_sam on 12/1/2016.
 */

public class LoggerDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public LoggerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + LogEntry.TABLE_NAME + " (" +
                LogEntry._ID + " INTEGER PRIMARY KEY," +
                LogEntry.COLUMN_HIGHLIGHT + " TEXT, " +
                LogEntry.COLUMN_LOCATION + " TEXT, " +
                LogEntry.COLUMN_DAY + " INTEGER NOT NULL, " +
                LogEntry.COLUMN_MONTH + " INTEGER NOT NULL, " +
                LogEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                LogEntry.COLUMN_PHOTOS + " TEXT " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LogEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
