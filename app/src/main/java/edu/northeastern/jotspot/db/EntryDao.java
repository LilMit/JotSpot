package edu.northeastern.jotspot.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

import edu.northeastern.jotspot.db.models.Entry;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM entries")
    LiveData<List<Entry>> getAllEntries();

    @Query("SELECT * FROM entries WHERE dateCreated = :dateCreated")
    List<Entry> findEntry(Date dateCreated);

    @Insert
    void addEntry(Entry entry);

    @Query("DELETE from entries WHERE entryId = :id")
    void deleteEntry(int id);
}
