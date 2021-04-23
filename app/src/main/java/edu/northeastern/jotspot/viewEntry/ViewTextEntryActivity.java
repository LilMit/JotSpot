package edu.northeastern.jotspot.viewEntry;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.ui.main.MainViewModel;

public class ViewTextEntryActivity extends AppCompatActivity {

    private TextView content;
    private Button backButton;
    private Button deleteButton;
    private TextView date;

    private String id;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_entry);
        content = findViewById(R.id.entry_text);
        backButton = findViewById(R.id.back_button);
        deleteButton = findViewById(R.id.delete_text_button);
        date = findViewById(R.id.text_entry_date);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> entry = extras.getStringArrayList("ENTRY");
        date.setText(entry.get(0));
        content.setText(entry.get(1));
        id = entry.get(2);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

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
    }
}