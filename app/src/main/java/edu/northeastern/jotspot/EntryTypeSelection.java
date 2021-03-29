package edu.northeastern.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import edu.northeastern.jotspot.newEntry.TextEntryActivity;

public class EntryTypeSelection extends AppCompatActivity {

    private ImageButton textEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_type_selection);
        textEntryButton = findViewById(R.id.select_text_entry);

        textEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EntryTypeSelection.this, TextEntryActivity.class);
                startActivity(i);
            }
        });
    }
}