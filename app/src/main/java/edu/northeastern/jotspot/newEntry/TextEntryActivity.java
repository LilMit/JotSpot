package edu.northeastern.jotspot.newEntry;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.ui.main.MainViewModel;

public class TextEntryActivity extends AppCompatActivity {

    private TextView contentEditText;
    private Button saveButton;
    private Entry currentEntry;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry);
        contentEditText = findViewById(R.id.text_entry_edit_text);
        saveButton = findViewById(R.id.save_button);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Date date = new Date(Instant.now().toEpochMilli());
                String content = contentEditText.getText().toString();
                Entry entry = new Entry(date, EntryType.TEXT, content);
                mainViewModel.insertEntry(entry);
                finish();
            }
        });

    }
}