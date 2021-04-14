package edu.northeastern.jotspot.newEntry;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.TextEntry;
import edu.northeastern.jotspot.ui.main.MainViewModel;

public class TextEntryActivity extends AppCompatActivity {

    private TextView contentEditText;
    private Button saveButton;

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
                TextEntry entry = new TextEntry(date, content);
                mainViewModel.insertEntry(entry);
                finish();
            }
        });
    }
}