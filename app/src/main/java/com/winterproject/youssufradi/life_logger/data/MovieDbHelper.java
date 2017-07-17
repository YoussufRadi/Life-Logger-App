package com.winterproject.youssufradi.life_logger.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.winterproject.youssufradi.life_logger.data.MovieContract.LogEntry;
/**
 * Created by y_sam on 12/1/2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + LogEntry.TABLE_NAME + " (" +
                LogEntry._ID + " INTEGER PRIMARY KEY," +
                LogEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                LogEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                LogEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                LogEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                LogEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                LogEntry.COLUMN_RATING + " TEXT NOT NULL " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LogEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
