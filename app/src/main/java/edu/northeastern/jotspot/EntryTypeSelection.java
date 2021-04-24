package edu.northeastern.jotspot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.jotspot.newEntry.AudioEntryActivity;
import edu.northeastern.jotspot.newEntry.TextEntryActivity;

public class EntryTypeSelection extends AppCompatActivity {

    private ImageButton textEntryButton;
    private ImageButton audioEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_type_selection);
        textEntryButton = findViewById(R.id.select_text_entry);
        audioEntryButton = findViewById(R.id.audio_entry_button);

        textEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EntryTypeSelection.this, TextEntryActivity.class);
                startActivity(i);
                finish();
            }
        });

        audioEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAudioEntryActivity();
                finish();
            }
        });

    }

    public void openAudioEntryActivity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(EntryTypeSelection.this, AudioEntryActivity.class);
                startActivity(i);;
            }
        }).start();
    }
}