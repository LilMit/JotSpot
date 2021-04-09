package edu.northeastern.jotspot.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(tableName = "entries")
public abstract class Entry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name= "entryId")
    private int id;

    @ColumnInfo(name = "dateCreated")
    private Timestamp timestamp;

    @ColumnInfo(name = "entryType")
    private EntryType type;

    public Entry(Timestamp timestamp, EntryType type) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public EntryType getType() {
        return type;
    }
}
