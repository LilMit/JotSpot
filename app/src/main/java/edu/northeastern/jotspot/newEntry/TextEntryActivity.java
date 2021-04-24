package edu.northeastern.jotspot.newEntry;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.ui.main.MainViewModel;

public class TextEntryActivity extends AppCompatActivity {

    private static String TAG = "TextActivity";
    private static String TEXT_KEY = "TEXT_KEY";
    private TextView contentEditText;
    private TextView entryDate;
    private Button saveButton;
    private Entry currentEntry;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry);

        Date date = new Date(Instant.now().toEpochMilli());
        currentEntry = new Entry(date, EntryType.TEXT);

        Log.e(TAG, "currentEntry =" + currentEntry.toString());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setSelectedEntry(currentEntry);
        mainViewModel.getSelectedEntry().observe(this, new
                Observer<Entry>() {
                    @Override
                    public void onChanged(Entry entry) {
                        currentEntry = entry;
                        Log.e(TAG, "currentEntry =" + currentEntry.toString());
                    }
                });

        contentEditText = findViewById(R.id.text_entry_edit_text);
        entryDate = findViewById(R.id.new_text_entry_date);
        saveButton = findViewById(R.id.save_button);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.moodbar_container_view, MoodFragment.class, null)
                    .commit();
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                currentEntry.setContent(content);
                mainViewModel.insertEntry(currentEntry);
                Log.e(TAG, "saving entry "+ currentEntry.toString());
                finish();
            }
        });

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ArrayList<String> text = savedInstanceState.getStringArrayList(TEXT_KEY);
        contentEditText.setText(text.get(0));
        entryDate.setText(text.get(1));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<String> text = new ArrayList<>();
        text.add((String) contentEditText.getText().toString());
        text.add((String) entryDate.getText());
        outState.putStringArrayList(TEXT_KEY, text);

        super.onSaveInstanceState(outState);
    }

}