package edu.northeastern.jotspot.db;

import androidx.room.TypeConverter;

import java.sql.Date;

import edu.northeastern.jotspot.db.models.EntryType;

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

    @TypeConverter
    public static String fromEntryType(EntryType type) {
        return type == null ? null : type.toString();
    }

    @TypeConverter
    public static EntryType stringToEntryType(String type) {
        return type == null ? null : EntryType.valueOf(type);
    }

}
