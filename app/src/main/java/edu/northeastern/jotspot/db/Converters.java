package edu.northeastern.jotspot.db;

import androidx.room.TypeConverter;

import java.sql.Date;

/**
 * Converts Date into usable datatype for Room Database.
 *
 * Code from docs https://developer.android.com/training/data-storage/room/referencing-data
 */
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
