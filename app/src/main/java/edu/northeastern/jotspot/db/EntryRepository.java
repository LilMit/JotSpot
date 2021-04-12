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

    private void asyncFinished(List<Entry> results){
        searchResults.setValue(results);
    }

    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Entry>> {
        private final EntryDao asyncTaskDao;
        private EntryRepository delegate = null;

        QueryAsyncTask(EntryDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected List<Entry> doInBackground(final String... params) {
            return asyncTaskDao.findEntry(Date.valueOf(params[0]));
        }

        @Override
        protected void onPostExecute(List<Entry> result){
            delegate.asyncFinished(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Entry, Void, Void> {

        private final EntryDao asyncTaskDao;

        InsertAsyncTask(EntryDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Entry... params) {
            asyncTaskDao.addEntry(params[0]);
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

    public void findEntry(String date){
        QueryAsyncTask task = new QueryAsyncTask(entryDao);
        task.delegate = this;
        task.execute(date);
    }

}
