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
    private Integer id;

    @ColumnInfo(name = "dateCreated")
    private Date date;

    @ColumnInfo(name = "entryType")
    private EntryType type;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "mood")
    private int mood;

    public Entry(){}

    public Entry(Date date, EntryType type) {
        this.date = date;
        this.type = type;
    }

    public Entry(Date date, EntryType type, String content) {
        this.date = date;
        this.type = type;
        this.content = content;
    }

    public Entry(Date date, EntryType type, String content, int mood) {
        this.date = date;
        this.type = type;
        this.content = content;
        this.mood = mood;
    }

    public Entry(Integer id, Date date, EntryType type, String content, int mood) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.content = content;
        this.mood = mood;
    }

    public Entry(Integer id, Date date, EntryType type, String content) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    @Override
    public String toString(){
        return type + " entry " + id + ": date: " + date + ", mood: " + mood;
    }
}
