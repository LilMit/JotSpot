package edu.northeastern.jotspot.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.northeastern.jotspot.db.models.Entry;

public class EntryRepository {
    private EntryDao entryDao;
    private EntryDatabase db;
    private LiveData<List<Entry>> allEntries;

    public EntryRepository(Application application) {
        db = EntryDatabase.getDatabase(application);
        entryDao = db.entryDao();
        allEntries = entryDao.getAllEntries();
    }

}
