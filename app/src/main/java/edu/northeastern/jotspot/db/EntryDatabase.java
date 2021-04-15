package edu.northeastern.jotspot.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.TextEntry;

/**
 * This was created by following Chapter 66 of Android Studio 4.1 Development Essentials
 */
@Database(entities = {Entry.class, TextEntry.class}, version=3)
@TypeConverters({Converters.class})
public abstract class EntryDatabase extends RoomDatabase {
    public abstract EntryDao entryDao();

    private static EntryDatabase INSTANCE;
    static EntryDatabase getDatabase(final Context context) {
        if (INSTANCE==null){
            synchronized (EntryDatabase.class) {
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EntryDatabase.class, "entry_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
