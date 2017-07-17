package com.winterproject.youssufradi.life_logger.data;

import android.provider.BaseColumns;

/**
 * Created by y_sam on 12/1/2016.
 */

public class MovieContract {

    public static final class LogEntry implements BaseColumns{
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATING = "rating";

    }
}
