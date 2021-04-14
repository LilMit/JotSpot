package edu.northeastern.jotspot.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "entries")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name= "entryId")
    private int id;

    @ColumnInfo(name = "dateCreated")
    private Date date;

    @ColumnInfo(name = "entryType")
    private EntryType type;

    public Entry(Date date, EntryType type) {
        this.id = id;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public EntryType getType() {
        return type;
    }
}
