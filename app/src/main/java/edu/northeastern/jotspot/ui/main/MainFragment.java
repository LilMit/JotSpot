package edu.northeastern.jotspot.ui.main;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.northeastern.jotspot.EntryTypeSelection;
import edu.northeastern.jotspot.MainActivity;
import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.ScheduleNotificationActivity;
import edu.northeastern.jotspot.SettingsActivity;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.viewEntry.ViewAudioEntryActivity;
import edu.northeastern.jotspot.viewEntry.ViewTextEntryActivity;

/**
 * This was initially created by following Chapter 68 of Android Studio 4.1 Development Essentials then modified
 */
public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;
    private EntryListAdapter adapter;

    public static String NOTIFICATION_CHANNEL = "edu.northeastern.jotspot.reminders";
    NotificationManager notificationManager;
    private static final int notificationId = 101;
    private static final String KEY_REMOTE_ENTRY = "key_remote_entry";

//    private TextView entryId;
//    private TextView entryTimestamp;
//    private TextView entryType;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
//    }

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

//        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        createNotificationChannel(NOTIFICATION_CHANNEL, "JotSpot Reminders",
//                "JotSpot Reminder Channel");
//
//        handleIntent();
//        sendNotification();
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

//    // sample code from docs https://developer.android.com/training/scheduling/alarms
//    public void scheduleNotification(int hour, int min){
//        AlarmManager alarmMgr;
//        PendingIntent alarmIntent;
//
//        alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getContext(), AlarmReceiver.class);
//        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//
//// Set the alarm to start at hour, min
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, min);
//
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, alarmIntent);
//
//
//    }
//
//    public class AlarmReceiver extends BroadcastReceiver {
//        String TAG = "AlarmReceiver";
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            sendNotification();
//        }
//    }

    public void sendNotification() {

        String channelId = NOTIFICATION_CHANNEL;
        String replyLabel = "Write your entry here.";

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REMOTE_ENTRY).setLabel(replyLabel)
                .build();

        Intent resultIntent = new Intent(getActivity(), EntryTypeSelection.class);
        Intent entryIntent = new Intent(getActivity(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent newEntryPendingIntent = PendingIntent.getActivity(getActivity(),0,entryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Icon icon = Icon.createWithResource(getActivity(), R.mipmap.ic_launcher);
        Notification.Action replyAction = new Notification.Action.Builder(icon, "New Entry",
                newEntryPendingIntent).addRemoteInput(remoteInput).build();

        Notification notification = new Notification.Builder(getActivity(), channelId)
                .setContentTitle("JotSpot Reminder").setContentText("Time to write an entry!")
                .setSmallIcon(R.mipmap.ic_launcher).setChannelId(channelId)
                .setContentIntent(pendingIntent).addAction(replyAction).build();

        notificationManager.notify(notificationId, notification);

    }

    private void handleIntent(){
        Intent intent = getActivity().getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput !=null){
            //add to database
            String inputString = remoteInput.getCharSequence(KEY_REMOTE_ENTRY).toString();
            Date date = new Date(Instant.now().toEpochMilli());
            Entry entry = new Entry(date, EntryType.TEXT, inputString);
            mainViewModel.insertEntry(entry);
        }

        Notification repliedNotification = new Notification.Builder(getActivity(), NOTIFICATION_CHANNEL).setSmallIcon(R.drawable.ic_launcher_foreground).setContentText("Entry saved.").build();

        notificationManager.notify(notificationId, repliedNotification);
    }

//    private void clearFields() {
//        entryId.setText("");
//        entryTimestamp.setText("");
//        entryType.setText("");
//    }

    private void listenerSetup() {
        ImageButton addButton = getView().findViewById(R.id.add_button);
        ImageButton findButton = getView().findViewById(R.id.search_button);
        ImageButton deleteButton = getView().findViewById(R.id.delete_button);
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
                Intent i = new Intent(MainFragment.this.getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

//        preferencesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainFragment.this.getContext(), SettingsActivity.class);
//                startActivity(i);
//            }
//        });

        //TODO reimplement
//        findButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainViewModel.findEntry(entryTimestamp.getText().toString());
//            }
//        });
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainViewModel.deleteEntry(entryId.getText().toString());
//                clearFields();
//            }
//        });

    }

    private void observerSetup() {
        mainViewModel.getAllEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                adapter.setEntryList(entries);
            }
        });
        //TODO reimplement in search later
//        mainViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
//            @Override
//            public void onChanged(List<Entry> entries) {
//                if (entries.size() > 0) {
//                    entryId.setText(String.format(Locale.US, "%d", entries.get(0).getId()));
//                    entryTimestamp.setText(entries.get(0).getTimestamp().toString());
//                    entryType.setText(String.format(Locale.US, "%d", entries.get(0).getType()));
//                } else {
//                    entryId.setText("No matching entries.");
//                }
//            }
//        });
    }

    private void recyclerSetup() {
        RecyclerView recyclerView;
        adapter = new EntryListAdapter(R.layout.entry_list_item);
        recyclerView = getView().findViewById(R.id.entry_recycler);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(Entry item) {
                int id = item.getId();
                EntryType type = item.getType();
                String content = item.getContent();
                String date = item.getDate().toString();
                Intent intent;
                if (type == EntryType.TEXT) {
                    intent = new Intent(MainFragment.this.getContext(), ViewTextEntryActivity.class);
                } else {
                    intent = new Intent(MainFragment.this.getContext(), ViewAudioEntryActivity.class);
                }
//                            Bundle bundle = new Bundle();
                ArrayList<String> info = new ArrayList<>();
                info.add(date);
                info.add(content);
//                            bundle.putStringArrayList("ENTRY", info);
                intent.putStringArrayListExtra("ENTRY", info);
//                            intent.putExtra("CONTENT", content);
                Log.e("Adapter", "about to start activity");
                startActivity(intent);
//                adapter.notifyItemChanged(position);
            }

        };
        adapter.setOnItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}