package edu.northeastern.jotspot.viewEntry;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private String id;

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
                if (!mStartPlaying) {
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
        fileName = entry.get(1);
        id = entry.get(2);

        LinearLayout vll = new LinearLayout(this);
        vll.setOrientation(LinearLayout.VERTICAL);

        timestamp = new TextView(this);
        timestamp.setText(startTime);
        timestamp.setTextSize(24);
        vll.addView(timestamp,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        LinearLayout ll = new LinearLayout(this);
        playButton = new PlayButton(this);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        deleteButton = new Button(this);
        deleteButton.setText("delete");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mainViewModel.deleteEntry(id);
                    File file = new File(fileName);
                    file.delete();
                    finish();
                }
        });
        ll.addView(deleteButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

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
        vll.addView(ll);
        setContentView(vll);
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