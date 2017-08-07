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
        public static final String COLUMN_PHOTOS = "photos";
    }

    public static final class EventEntry implements BaseColumns{
        public static final String TABLE_NAME = "EventEntries";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_START_DAY = "startDay";
        public static final String COLUMN_START_MONTH = "startMonth";
        public static final String COLUMN_START_YEAR = "startYear";
        public static final String COLUMN_START_HOUR = "startHour";
        public static final String COLUMN_START_MINUTE = "startMinute";
        public static final String COLUMN_END_DAY = "endDay";
        public static final String COLUMN_END_MONTH = "endMonth";
        public static final String COLUMN_END_YEAR = "endYear";
        public static final String COLUMN_END_HOUR = "endHour";
        public static final String COLUMN_END_MINUTE = "endMinute";
        public static final String COLUMN_LOGS = "logs";
        public static final String COLUMN_PEOPLE_NAME = "peopleName";
        public static final String COLUMN_PEOPLE_NUMBER = "peopleNumber";
    }


    public static final class PhotoEntry implements BaseColumns{
        public static final String TABLE_NAME = "photoEntries";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PHOTOS = "photos";
        public static final String COLUMN_PEOPLE_NAME = "peopleName";
        public static final String COLUMN_PEOPLE_NUMBER = "peopleNumber";
    }

    public static final class VoiceEntry implements BaseColumns{
        public static final String TABLE_NAME = "voiceEntries";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
