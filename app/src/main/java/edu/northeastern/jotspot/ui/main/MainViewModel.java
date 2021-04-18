package edu.northeastern.jotspot.ui.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import edu.northeastern.jotspot.db.EntryRepository;
import edu.northeastern.jotspot.db.models.Entry;

/**
 * This was created by following Chapter 68 of Android Studio 4.1 Development Essentials
 */
public class MainViewModel extends AndroidViewModel {
    private EntryRepository repository;
    private LiveData<List<Entry>> allEntries;
    private MutableLiveData<List<Entry>> searchResults;
    private MutableLiveData<Entry> selectedEntry;

    public MainViewModel(Application application) {
        super(application);
        repository = new EntryRepository(application);
        allEntries = repository.getAllEntries();
        searchResults = repository.getSearchResults();
        selectedEntry = repository.getSelectedEntry();
    }

    public LiveData<List<Entry>> getAllEntries() {
        return allEntries;
    }

    public MutableLiveData<List<Entry>> getSearchResults() {
        return searchResults;
    }

    public MutableLiveData<Entry> getSelectedEntry() {
        return selectedEntry;
    }

    public void insertEntry(Entry entry) {
        repository.insertEntry(entry);
    }

    public void findEntries(String date) {
        repository.findEntries(date);
    }

    public void findEntry(String id) {
        repository.findEntry(id);
    }

    public void deleteEntry(String id) {
        repository.deleteEntry(id);
    }
}
