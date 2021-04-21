package edu.northeastern.jotspot.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

import edu.northeastern.jotspot.db.models.Entry;

/**
 * This was created by following Chapter 66 of Android Studio 4.1 Development Essentials
 */
@Dao
public interface EntryDao {

    @Query("SELECT * FROM entries")
    LiveData<List<Entry>> getAllEntries();

    // dateCreated is stored as long representing ms. there are 86400000 ms in a day and the search date is a time at midnight
    @Query("SELECT * FROM entries WHERE dateCreated BETWEEN :inputDate AND :nextDay")
    List<Entry> findEntries(Date inputDate, Date nextDay);

    @Query("SELECT * FROM entries WHERE entryId== :id")
    Entry findEntry(int id);

    @Insert
    void addEntry(Entry entry);

    @Query("DELETE from entries WHERE entryId = :id")
    void deleteEntry(int id);


}
