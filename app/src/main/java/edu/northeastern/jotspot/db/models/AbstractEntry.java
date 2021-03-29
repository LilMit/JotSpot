package edu.northeastern.jotspot.db.models;

import java.sql.Timestamp;

public abstract class AbstractEntry {
    private Timestamp timestamp;
    private EntryType type;

    public AbstractEntry(Timestamp timestamp, EntryType type) {
        this.timestamp = timestamp;
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public EntryType getType() {
        return type;
    }
}
