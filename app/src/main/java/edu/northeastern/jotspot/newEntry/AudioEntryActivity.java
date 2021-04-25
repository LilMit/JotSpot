package edu.northeastern.jotspot.newEntry;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.ui.main.MainViewModel;

/**
 * This code is heavily based on MediaRecorder code from Android documentation
 * https://developer.android.com/guide/topics/media/mediarecorder
 */
public class AudioEntryActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecording";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private MainViewModel mainViewModel;

    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    private PlayButton playButton = null;
    private MediaPlayer player = null;

    private Button saveButton =null;
    private Button deleteButton =null;

    private Date startTime = null;
    private Date endTime = null;

    Entry currentEntry;

    private boolean recordPermissionAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            recordPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!recordPermissionAccepted) finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare failed");
        }
        recorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void stopRecording() {
        recorder.stop();
        endTime = new Date(Instant.now().toEpochMilli());
        recorder.release();
        recorder = null;
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                    playButton.setEnabled(false);
                    saveButton.setEnabled(false);
                } else {
                    setText("Start recording");
                    playButton.setEnabled(true);
                    saveButton.setEnabled(true);
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context context) {
            super(context);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Pause");
                } else {
                    setText("Play");
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
        //int buttonColor = getResources().getColor(R.color.teal);
        super.onCreate(savedInstanceState);
        startTime = new Date(Instant.now().toEpochMilli());
        currentEntry = new Entry(startTime, EntryType.AUDIO);
        String stamp = new SimpleDateFormat("MM-dd-yyyy_HHmmss").format(startTime);
        String storageDirectory = getApplicationContext().getFilesDir().getAbsolutePath();
        Log.e(LOG_TAG, "StorageDirectory =" + storageDirectory);
        File audioDirectory = new File(storageDirectory + "/JotSpot/Audio");
        if(!audioDirectory.exists()){
            if(!audioDirectory.mkdirs()){
                Log.e(LOG_TAG, "Failed to create " + audioDirectory.getPath());
            }
        }

        fileName = audioDirectory.getAbsolutePath() + stamp + ".3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setSelectedEntry(currentEntry);
        mainViewModel.getSelectedEntry().observe(this, new
                Observer<Entry>() {
                    @Override
                    public void onChanged(Entry entry) {
                        currentEntry = entry;
                    }
                });
        LinearLayout vll = new LinearLayout(this);
        vll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout ll = new LinearLayout(this);

        recordButton = new RecordButton(this);
        ll.addView(recordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        playButton = new PlayButton(this);
        playButton.setEnabled(false);
        //playButton.setBackgroundColor(buttonColor);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        saveButton = new Button(this);
        saveButton.setText("Save");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endTime == null){
                    Toast.makeText(AudioEntryActivity.this, "Nothing to save", Toast.LENGTH_LONG).show();
                } else {
                    String content = fileName;
                    currentEntry.setContent(content);
                    mainViewModel.insertEntry(currentEntry);
                    finish();
                }
            }
        });
        saveButton.setEnabled(false);
        ll.addView(saveButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        vll.addView(ll);
        FragmentContainerView moodBarContainerView = new FragmentContainerView(this);
        moodBarContainerView.setId(R.id.moodbar_container_view);
        vll.addView(moodBarContainerView);
        setContentView(vll);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.moodbar_container_view, MoodFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void onStop(){
        super.onStop();;
        if (recorder!=null){
            recorder.release();
            recorder = null;
        }

        if(player !=null){
            player.release();
            player = null;
        }
    }

    @Override
    public void onBackPressed(){
        File file = new File(fileName);
        file.delete();
        super.onBackPressed();
    }

}