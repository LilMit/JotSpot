package edu.northeastern.jotspot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.northeastern.jotspot.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

//    public static String NOTIFICATION_CHANNEL = "edu.northeastern.jotspot.reminders";
//    NotificationManager notificationManager;
//    private static final int notificationId = 101;
//    private static final String KEY_REMOTE_ENTRY = "key_remote_entry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        createNotificationChannel(NOTIFICATION_CHANNEL, "JotSpot Reminders",
//                "JotSpot Reminder Channel");
//        sendNotification();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

//        handleIntent();

    }

//    protected void createNotificationChannel(String id, String name, String description) {
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel channel = new NotificationChannel(id, name, importance);
//
//        channel.setDescription(description);
//        channel.enableLights(true);
//        channel.setLightColor(Color.MAGENTA);
//        channel.enableVibration(true);
//        channel.setVibrationPattern(new long[]{400});
//        notificationManager.createNotificationChannel(channel);
//    }
//
//    public void sendNotification() {
//
//        String channelId = NOTIFICATION_CHANNEL;
//        String replyLabel = "Write your entry here.";
//
//        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REMOTE_ENTRY).setLabel(replyLabel)
//                .build();
//
//        Intent resultIntent = new Intent(this, EntryTypeSelection.class);
//        Intent entryIntent = new Intent(this, MainActivity.class);
//
//        PendingIntent pendingIntent = PendingIntent
//                .getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        PendingIntent newEntryPendingIntent = PendingIntent.getActivity(this,0,entryIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        final Icon icon = Icon.createWithResource(this, R.drawable.ic_launcher_foreground);
//        Notification.Action replyAction = new Notification.Action.Builder(icon, "New Entry",
//                newEntryPendingIntent).addRemoteInput(remoteInput).build();
//
//        Notification notification = new Notification.Builder(MainActivity.this, channelId)
//                .setContentTitle("JotSpot Reminder").setContentText("Time to write an entry!")
//                .setSmallIcon(R.drawable.ic_launcher_foreground).setChannelId(channelId)
//                .setContentIntent(pendingIntent).addAction(replyAction).build();
//
//        notificationManager.notify(notificationId, notification);
//
//    }
//
//    private void handleIntent(){
//        Intent intent = this.getIntent();
//
//        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
//        if (remoteInput !=null){
//            //add to database
//        }
//
//        Notification repliedNotification = new Notification.Builder(this, NOTIFICATION_CHANNEL).setSmallIcon(R.drawable.ic_launcher_foreground).setContentText("Entry saved.").build();
//
//        notificationManager.notify(notificationId, repliedNotification);
//    }
}

