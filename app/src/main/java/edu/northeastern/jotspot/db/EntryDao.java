package edu.northeastern.jotspot.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.TextEntry;

/**
 * This was created by following Chapter 66 of Android Studio 4.1 Development Essentials
 */
@Dao
public interface EntryDao {

    @Query("SELECT * FROM entries")
    LiveData<List<Entry>> getAllEntries();

    @Query("SELECT * FROM entries WHERE dateCreated = :dateCreated")
    List<Entry> findEntry(Date dateCreated);

//    //TODO: correct so actually finds text of keyword
//    @Query("SELECT * FROM textEntries WHERE content LIKE '%:keyword%'")
//    List<Entry> findEntry(String keyword);

    @Insert
    void addEntry(Entry entry);

    @Insert
    void addTextEntry(TextEntry entry);

    @Query("DELETE from entries WHERE entryId = :id")
    void deleteEntry(int id);


}
