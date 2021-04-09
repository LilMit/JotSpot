package edu.northeastern.jotspot.ui.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import edu.northeastern.jotspot.db.EntryRepository;
import edu.northeastern.jotspot.db.models.Entry;

public class MainViewModel extends AndroidViewModel {
    private EntryRepository repository;
    private LiveData<List<Entry>> allEntries;
    private MutableLiveData<List<Entry>> searchResults;

    public MainViewModel(Application application){
        super(application);
        repository =new EntryRepository(application);
        allEntries = repository.getAllEntries();
        searchResults = repository.getSearchResults();
    }

    public LiveData<List<Entry>> getAllEntries(){
        return allEntries;
    }

    public MutableLiveData<List<Entry>> getSearchResults(){
        return searchResults;
    }

    public void insertEntry(Entry entry){
        repository.insertEntry(entry);
    }

    public void findEntry(String date){
        repository.findEntry(date);
    }

    public void deleteEntry(String id){
        repository.deleteEntry(id);
    }
}
