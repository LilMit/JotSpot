package edu.northeastern.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

import edu.northeastern.jotspot.ui.main.MainFragment;

public class ScheduleNotificationActivity extends AppCompatActivity {

    private final String TAG  = "ScheduleActivity";
    public static String NOTIFICATION_CHANNEL = "edu.northeastern.jotspot.reminders";
    public static NotificationManager notificationManager;
    private static final int notificationId = 101;
    private static final String KEY_REMOTE_ENTRY = "key_remote_entry";
    private static final int ALARM_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_notification);
        notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(NOTIFICATION_CHANNEL, "JotSpot Reminders",
                "JotSpot Reminder Channel");
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

    // sample code from docs https://developer.android.com/training/scheduling/alarms
    public void scheduleNotification(int hour, int min) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, 0);

// Set the alarm to start at hour, min
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        // cancel already scheduled reminders
        cancelReminder(this, AlarmReceiver.class);
        // delete prior schedule
        if (calendar.before(calendar))
            calendar.add(Calendar.DATE, 1);

        ComponentName receiver = new ComponentName(this, AlarmReceiver.class);

        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);


    }

    // from https://droidmentor.com/schedule-notifications-using-alarmmanager/
    public static void cancelReminder(Context context, Class<?> cls) {

        // Disable receiver
        ComponentName receiver = new ComponentName(context, cls);

        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                ALARM_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();

    }

    public static class AlarmReceiver extends BroadcastReceiver {
        String TAG = "AlarmReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            sendNotification(context);
        }
    }

    public static void sendNotification(Context context) {

        String channelId = NOTIFICATION_CHANNEL;
        String replyLabel = "Write your entry here.";

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REMOTE_ENTRY).setLabel(replyLabel)
                .build();

        //TODO check usage of package contexts of intents, should it be "this" or something else?

        // set up intent for clicking main notification body to create new entry from type selection
        Intent openTypeSelectionIntent = new Intent(context, EntryTypeSelection.class);
        PendingIntent typeSelectionPendingIntent = PendingIntent
                .getActivity(context, 0, openTypeSelectionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // set up intent for creating new entry directly from notification
        Intent entryIntent = new Intent(context, MainActivity.class);
        PendingIntent newEntryPendingIntent = PendingIntent
                .getActivity(context, 0, entryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Icon icon = Icon.createWithResource(context, R.mipmap.ic_launcher);
        Notification.Action replyAction = new Notification.Action.Builder(icon, "New Entry",
                newEntryPendingIntent).addRemoteInput(remoteInput).build();

        Notification notification = new Notification.Builder(context, channelId)
                .setContentTitle("JotSpot Reminder").setContentText("Time to write an entry!")
                .setSmallIcon(R.mipmap.ic_launcher).setChannelId(channelId)
                .setContentIntent(typeSelectionPendingIntent).addAction(replyAction).build();

        notificationManager.notify(notificationId, notification);

    }

}