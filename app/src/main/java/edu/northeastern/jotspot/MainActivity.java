package edu.northeastern.jotspot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.northeastern.jotspot.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    public static String NOTIFICATION_CHANNEL = "edu.northeastern.jotspot.reminders";
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(NOTIFICATION_CHANNEL, "JotSpot Reminders",
                "JotSpot Reminder Channel");
        sendNotification();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

    }

    protected void createNotificationChannel(String id, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.MAGENTA);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{400});
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification() {

        String channelId = NOTIFICATION_CHANNEL;
        int notificationId = 101;

        Intent resultIntent = new Intent(this, EntryTypeSelection.class);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(MainActivity.this, channelId)
                .setContentTitle("JotSpot Reminder").setContentText("Time to write an entry!")
                .setSmallIcon(R.drawable.ic_launcher_foreground).setChannelId(channelId)
                .setContentIntent(pendingIntent).build();

        notificationManager.notify(notificationId, notification);

    }
}

