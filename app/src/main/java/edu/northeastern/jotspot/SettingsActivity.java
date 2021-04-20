package edu.northeastern.jotspot;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreference sendNotifications = findPreference("send_notifications");
            DialogPreference schedulePreference = findPreference("reminder_time");

            //String scheduleSummaryProvider = Preference.SummaryProvider(preference)

//            if (schedulePreference != null) {
//                schedulePreference.setOnBindEditTextListener(
//                        new EditTextPreference.OnBindEditTextListener() {
//                            @Override
//                            public void onBindEditText(@NonNull EditText editText) {
//                                editText.setInputType(InputType.TYPE_CLASS_DATETIME);
//                            }
//                        });
//            }

        }
    }
    public class TimePreference extends DialogPreference implements Preference.OnPreferenceChangeListener{
        TimePickerDialog picker;

        public TimePreference(Context context, AttributeSet attrs) {
            super(context, attrs, 0);
            this.setOnPreferenceChangeListener(this);
        }



        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return false;
        }


    }

    // based on sample code from https://developer.android.com/guide/topics/ui/controls/pickers
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

        }
    }

}