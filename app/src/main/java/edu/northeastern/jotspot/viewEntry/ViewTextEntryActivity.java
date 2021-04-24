package edu.northeastern.jotspot.viewEntry;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Date;
import java.util.ArrayList;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.newEntry.MoodFragment;
import edu.northeastern.jotspot.ui.main.MainViewModel;

public class ViewTextEntryActivity extends AppCompatActivity {

    private static final String TAG = "viewTextEntry";

    private TextView content;
    private Button backButton;
    private Button deleteButton;
    private TextView date;
    private ImageView moodImageView;
    private TextView moodLabel;
    private ImageButton editEntryTextButton;
    private ImageButton editEntryMoodButton;
    private Entry currentEntry;

    private String id;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_entry);
        content = findViewById(R.id.entry_text);
        backButton = findViewById(R.id.back_button);
        deleteButton = findViewById(R.id.delete_text_button);
        editEntryTextButton = findViewById(R.id.edit_text_button);
        editEntryMoodButton = findViewById(R.id.edit_mood_button);

        date = findViewById(R.id.text_entry_date);
        moodImageView = findViewById(R.id.mood_image_text_view);
        moodLabel = findViewById(R.id.moodLabelTextEntryView);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> entryExtra = extras.getStringArrayList("ENTRY");
        currentEntry = new Entry(Integer.parseInt(entryExtra.get(2)), Date.valueOf(entryExtra.get(0)), EntryType.TEXT, entryExtra.get(1),Integer.parseInt(entryExtra.get(3)));
        date.setText(currentEntry.getDate().toString());
        content.setText(currentEntry.getContent());
        id = currentEntry.getId().toString();
        setMood(currentEntry.getMood());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setSelectedEntry(currentEntry);
        Log.e(TAG, "selected item is now " + currentEntry);
        mainViewModel.getSelectedEntry().observe(this, new
                Observer<Entry>() {
                    @Override
                    public void onChanged(Entry entry) {
                        currentEntry = entry;
                        date.setText(currentEntry.getDate().toString());
                        content.setText(currentEntry.getContent());
                        setMood(currentEntry.getMood());
                        Log.e(TAG, "currentEntry =" + entry.toString());
                    }
                });


        backButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.deleteEntry(id);
                finish();
            }
        });

        editEntryTextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ViewTextEntryActivity.this.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.edit_entry_container_view, EditEntryFragment.class, null)
                        .commit();
            }
        });

        editEntryMoodButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ViewTextEntryActivity.this.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.edit_mood_entry_container_view, MoodFragment.class, null)
                        .commit();
            }
        });


    }

    public void setMood(int mood) {
        moodImageView.setVisibility(View.VISIBLE);
        moodLabel.setVisibility(View.VISIBLE);
        switch(mood){
            case 0:
                moodImageView.setVisibility(View.INVISIBLE);
                moodLabel.setVisibility(View.INVISIBLE);
                break;
            case 1:
                moodImageView.setImageResource(R.drawable.ic_worst_face);
                break;
            case 2:
                moodImageView.setImageResource(R.drawable.ic_bad_face);
                break;
            case 3:
                moodImageView.setImageResource(R.drawable.ic_meh_face);
                break;
            case 4:
                moodImageView.setImageResource(R.drawable.ic_satisfied_face);
                break;
            case 5:
                moodImageView.setImageResource(R.drawable.ic_happy_face);
                break;
        }
    }
}