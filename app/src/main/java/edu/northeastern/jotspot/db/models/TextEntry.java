package edu.northeastern.jotspot.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "textEntries")
public class TextEntry extends Entry {

    @NonNull
    @ForeignKey(entity = Entry.class, parentColumns = "id", childColumns = "id")
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="content")
    private String content;

    public TextEntry(Date timestamp) {
        super(timestamp, EntryType.TEXT);
    }

    public TextEntry(Date timestamp, String content) {
        super(timestamp, EntryType.TEXT);
        this.content = content;
    }

    public TextEntry(){}

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
