package edu.northeastern.jotspot.viewEntry;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.ui.main.MainViewModel;

public class ViewTextEntryActivity extends AppCompatActivity {

    private static final String TAG = "viewTextEntry";

    private TextView content;
    private Button backButton;
    private Button deleteButton;
    private TextView date;
    private ImageView moodImageView;
    private TextView moodLabel;
    private Button editButton;

    private String id;
    private int mood;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_entry);
        content = findViewById(R.id.entry_text);
        backButton = findViewById(R.id.back_button);
        deleteButton = findViewById(R.id.delete_text_button);
        date = findViewById(R.id.text_entry_date);
        moodImageView = findViewById(R.id.mood_image_text_view);
        moodLabel = findViewById(R.id.moodLabelTextEntryView);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> entryExtra = extras.getStringArrayList("ENTRY");
        date.setText(entryExtra.get(0));
        content.setText(entryExtra.get(1));
        id = entryExtra.get(2);
        mood = Integer.parseInt(entryExtra.get(3));


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

//        editButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        setMood(mood);
    }

    public void setMood(int mood) {

        switch(mood){
            case 0:
                moodImageView.setVisibility(View.INVISIBLE);
                moodLabel.setVisibility(View.INVISIBLE);
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