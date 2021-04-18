package edu.northeastern.jotspot.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.sql.Date;
import java.util.List;

import edu.northeastern.jotspot.db.models.Entry;

/**
 * This was created by following Chapter 66 of Android Studio 4.1 Development Essentials
 */
public class EntryRepository {

    private final MutableLiveData<List<Entry>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Entry> selectedEntry = new MutableLiveData<>();


    private EntryDao entryDao;
    private EntryDatabase db;
    private LiveData<List<Entry>> allEntries;

    public EntryRepository(Application application) {
        db = EntryDatabase.getDatabase(application);
        entryDao = db.entryDao();
        allEntries = entryDao.getAllEntries();
    }

    public LiveData<List<Entry>> getAllEntries(){
        return this.allEntries;
    }

    public MutableLiveData<List<Entry>> getSearchResults(){
        return this.searchResults;
    }
    public MutableLiveData<Entry> getSelectedEntry(){
        return this.selectedEntry;
    }


    private void asyncFinished(List<Entry> results){
        searchResults.setValue(results);
    }
    private void asyncFinishedOne(Entry result){
        selectedEntry.setValue(result);
    }

    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Entry>> {
        private final EntryDao asyncTaskDao;
        private EntryRepository delegate = null;

        QueryAsyncTask(EntryDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected List<Entry> doInBackground(final String... params) {
            return asyncTaskDao.findEntries(Date.valueOf(params[0]));
        }

        @Override
        protected void onPostExecute(List<Entry> result){
            delegate.asyncFinished(result);
        }
    }

    private static class FindOneAsyncTask extends AsyncTask<String, Void, Entry> {
        private final EntryDao asyncTaskDao;
        private EntryRepository delegate = null;

        FindOneAsyncTask(EntryDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Entry doInBackground(final String... params) {
            return asyncTaskDao.findEntry(Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(Entry result){
            delegate.asyncFinishedOne(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Entry, Void, Void> {

        private final EntryDao asyncTaskDao;

        InsertAsyncTask(EntryDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Entry... params) {
            Entry entry = params[0];
            asyncTaskDao.addEntry(entry);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {
        private final EntryDao asyncTaskDao;

        DeleteAsyncTask(EntryDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            asyncTaskDao.deleteEntry(Integer.parseInt(params[0]));
            return null;
        }

    }

    public void insertEntry(Entry newEntry){
        InsertAsyncTask task = new InsertAsyncTask(entryDao);
        task.execute(newEntry);
    }

    public void deleteEntry(String id){
        DeleteAsyncTask task = new DeleteAsyncTask(entryDao);
        task.execute(id);
    }

    public void findEntries(String date){
        QueryAsyncTask task = new QueryAsyncTask(entryDao);
        task.delegate = this;
        task.execute(date);
    }

    public void findEntry(String id){
        FindOneAsyncTask task = new FindOneAsyncTask(entryDao);
        task.delegate = this;
        task.execute(id);
    }

}
