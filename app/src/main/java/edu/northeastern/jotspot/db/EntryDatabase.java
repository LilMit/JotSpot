package edu.northeastern.jotspot.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.northeastern.jotspot.db.models.Entry;

@Database(entities = {Entry.class}, version=1)
public abstract class EntryDatabase extends RoomDatabase {
    public abstract EntryDao entryDao();

    private static EntryDatabase INSTANCE;
    static EntryDatabase getDatabase(final Context context) {
        if (INSTANCE==null){
            synchronized (EntryDatabase.class) {
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EntryDatabase.class, "entry_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
