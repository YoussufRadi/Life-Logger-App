package com.winterproject.youssufradi.life_logger.data;

import android.provider.BaseColumns;

/**
 * Created by y_sam on 12/1/2016.
 */

public class LoggerContract {

    public static final class LogEntry implements BaseColumns{
        public static final String TABLE_NAME = "logEntries";
        public static final String COLUMN_HIGHLIGHT = "highlight";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_PHOTOS = "date";

    }
}
