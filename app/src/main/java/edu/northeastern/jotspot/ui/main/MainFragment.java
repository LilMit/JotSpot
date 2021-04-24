package edu.northeastern.jotspot.ui.main;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.northeastern.jotspot.EntryTypeSelection;
import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.newEntry.AudioEntryActivity;
import edu.northeastern.jotspot.settings.SettingsActivity;
import edu.northeastern.jotspot.viewEntry.ViewAudioEntryActivity;
import edu.northeastern.jotspot.viewEntry.ViewTextEntryActivity;

import static java.util.Arrays.binarySearch;

/**
 * This was initially created by following Chapter 68 of Android Studio 4.1 Development
 * Essentials then modified
 */
public class MainFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    private MainViewModel mainViewModel;
    private EntryListAdapter adapter;

    public static String NOTIFICATION_CHANNEL = "edu.northeastern.jotspot.reminders";
    public static String TAG = "MainFragment";
    NotificationManager notificationManager;
    private static final int notificationId = 101;
    private static final String KEY_REMOTE_ENTRY = "key_remote_entry";
    private static int totalEntries;

    private DatePickerDialog datePickerDialog;
    private TextView entryCountTextView;
    private ImageButton refreshButton;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        entryCountTextView = getView().findViewById(R.id.entry_count_textview);


        datePickerDialog = makeDatePicker();

        notificationManager = (NotificationManager) getActivity().getSystemService(
                Context.NOTIFICATION_SERVICE);
        createNotificationChannel(NOTIFICATION_CHANNEL, "JotSpot Reminders",
                "JotSpot Reminder Channel");
        handleIntent();

        listenerSetup();
        observerSetup();
        recyclerSetup();
    }

    protected void createNotificationChannel(String id, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.MAGENTA);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{400});
        notificationManager.createNotificationChannel(channel);
    }

    private void handleIntent() {
        Intent intent = getActivity().getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            //add to database
            String inputString = remoteInput.getCharSequence(KEY_REMOTE_ENTRY).toString();
            Date date = new Date(Instant.now().toEpochMilli());
            Entry entry = new Entry(date, EntryType.TEXT, inputString);
            mainViewModel.insertEntry(entry);
            Notification repliedNotification = new Notification.Builder(getActivity(),
                    NOTIFICATION_CHANNEL).setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Entry saved.").build();

            notificationManager.notify(notificationId, repliedNotification);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new Date(year-1900,month,dayOfMonth);
        Log.e(TAG, "searching for entries from date " + date);
        mainViewModel.findEntries(String.valueOf(date));
    }

    private DatePickerDialog makeDatePicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

//    private void clearFields() {
//        entryId.setText("");
//        entryTimestamp.setText("");
//        entryType.setText("");
//    }

    private void listenerSetup() {
        ImageButton addButton = getView().findViewById(R.id.add_button);
        ImageButton findButton = getView().findViewById(R.id.search_button);
        refreshButton = getView().findViewById(R.id.refresh_button);
        ImageButton preferencesButton = getView().findViewById(R.id.preferencesButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainFragment.this.getContext(), EntryTypeSelection.class);
                startActivity(i);
            }
        });

        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferencesActivity();
            }
        });

        findButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        }));

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEntryList(mainViewModel.getAllEntries().getValue());
                entryCountTextView.setText(totalEntries + " entries");
            }
        });

    }

    private void observerSetup() {
        mainViewModel.getAllEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                adapter.setEntryList(entries);
                totalEntries = entries.size();
                entryCountTextView.setText(totalEntries + " entries");
                refreshButton.setEnabled(false);
                if(calculateReward(totalEntries)){
                    String ticker = totalEntries + " entries! Great job!";
                    Toast.makeText(MainFragment.this.getActivity(), ticker, Toast.LENGTH_LONG).show();
                }
            }
        });

        mainViewModel.getSearchResults().observe(getViewLifecycleOwner(), new
        Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                if (entries.size() > 0) {
                    adapter.setEntryList(entries);}
                    String entriesFoundText = entries.size() + " entries found.";
                    entryCountTextView.setText(entriesFoundText);
                    refreshButton.setEnabled(true);
            }
        });
    }

    private void recyclerSetup() {
        RecyclerView recyclerView;
        adapter = new EntryListAdapter(R.layout.entry_list_item);
        recyclerView = getView().findViewById(R.id.entry_recycler);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(Entry item) {
                mainViewModel.setSelectedEntry(item);
                Log.e(TAG, "selected item is now " + item);
                int id = item.getId();
                EntryType type = item.getType();
                String content = item.getContent();
                String date = String.valueOf(item.getDate().getTime());
                String mood = String.valueOf(item.getMood());
                Intent intent;
                if (type == EntryType.TEXT) {
                    intent = new Intent(MainFragment.this.getContext(),
                            ViewTextEntryActivity.class);
                } else {
                    intent = new Intent(MainFragment.this.getContext(),
                            ViewAudioEntryActivity.class);
                }
                ArrayList<String> info = new ArrayList<>();
                info.add(date);
                info.add(content);
                info.add(String.valueOf(id));
                info.add(mood);
                intent.putStringArrayListExtra("ENTRY", info);
                startActivity(intent);
            }

        };
        adapter.setOnItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private boolean calculateReward(int numEntries){
        int[] numbers = getResources().getIntArray(R.array.rewardNumbers);
        return binarySearch(numbers, numEntries)>=0;
    }

    public void openPreferencesActivity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainFragment.this.getContext(), SettingsActivity.class);
                startActivity(i);
            }
        }).start();
    }
}