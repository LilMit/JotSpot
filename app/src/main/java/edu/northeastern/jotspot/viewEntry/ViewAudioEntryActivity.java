package edu.northeastern.jotspot.viewEntry;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;

import edu.northeastern.jotspot.db.EntryRepository;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.ui.main.MainViewModel;

/**
 * This code is heavily based on MediaRecorder code from Android documentation
 * https://developer.android.com/guide/topics/media/mediarecorder
 */
public class ViewAudioEntryActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ViewAudioRecording";
    private static String fileName = null;

    private MainViewModel mainViewModel;

    private PlayButton playButton = null;
    private MediaPlayer player = null;

    private Button deleteButton =null;
    private Button backButton = null;

    private String startTime = null;
    private TextView timestamp = null;

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Play");
                } else {
                    setText("Pause");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context context) {
            super(context);
            setText("Play");
            setOnClickListener(clicker);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> entry = extras.getStringArrayList("ENTRY");
        startTime = entry.get(0);
        //startTime = Date.valueOf(entry.get(0));
        fileName = entry.get(1);

        LinearLayout ll = new LinearLayout(this);
        timestamp = new TextView(this);
        timestamp.setText(startTime);
        ll.addView(timestamp,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));


        playButton = new PlayButton(this);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

//        deleteButton = new Button(this);
//        deleteButton.setText("delete");
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    String content = fileName;
//                    Entry entry = new Entry(startTime, EntryType.AUDIO, content);
//                    mainViewModel.deleteEntry(entry);
//                    finish();
//                }
//            }
//        });
//        ll.addView(deleteButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));

        backButton = new Button(this);
        backButton.setText("back");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
                }
        });
        ll.addView(backButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);

        // setContentView(R.layout.activity_audio_entry);
    }

    @Override
    public void onStop(){
        super.onStop();;

        if(player !=null){
            player.release();
            player = null;
        }
    }

}