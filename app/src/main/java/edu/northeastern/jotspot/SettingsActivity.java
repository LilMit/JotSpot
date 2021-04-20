package edu.northeastern.jotspot;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
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
import androidx.preference.PreferenceDialogFragmentCompat;
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
            TimePreference schedulePreference = findPreference("reminder_time");

            //String scheduleSummaryProvider = Preference.SummaryProvider(preference)

        }
        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            // Try if the preference is one of our custom Preferences
            DialogFragment dialogFragment = null;
            if (preference instanceof TimePreference) {
                // Create a new instance of TimePreferenceDialogFragment with the key of the related
                // Preference
                Log.e("Settings", "TimePreference detected");
                dialogFragment = TimePreferenceDialogFragmentCompat
                        .newInstance(preference.getKey());
            }

            // If it was one of our cutom Preferences, show its dialog
            if (dialogFragment != null) {
                Log.e("Settings", "trying to show dialog");
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(),
                        "android.support.v7.preference" +
                                ".PreferenceFragment.DIALOG");
            }
            // Could not be handled here. Try with the super method.
            else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }


    /**
     * I tried figuring out how to implement a time picker in a preference before finally using
     * https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec
     */
    public class TimePreference extends DialogPreference implements Preference.OnPreferenceChangeListener {
        TimePickerDialog picker;
        private int mTime;
        private int mDialogLayoutResId = R.layout.pref_time_dialog;

        public TimePreference(Context context) {
            this(context, null);
        }

        public TimePreference(Context context, AttributeSet attrs) {
            super(context, attrs, R.attr.dialogPreferenceStyle);
        }

        public TimePreference(Context context, AttributeSet attrs,
                int defStyleAttr) {
            this(context, attrs, defStyleAttr, defStyleAttr);
        }

        public TimePreference(Context context, AttributeSet attrs,
                int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);

            // Do custom stuff here
            // ...
            // read attributes etc.
        }

        public int getTime() {
            return mTime;
        }

        public void setTime(int time) {
            mTime = time;    // Save to Shared Preferences
            persistInt(time);
        }

        @Override
        protected Object onGetDefaultValue(TypedArray a, int index) {
            // Default value from attribute. Fallback value is set to 0.
            return a.getInt(index, 0);
        }

        @Override
        protected void onSetInitialValue(boolean restorePersistedValue,
                Object defaultValue) {
            // Read the value. Use the default value if it is not possible.
            setTime(restorePersistedValue ?
                    getPersistedInt(mTime) : (int) defaultValue);
        }

        @Override
        public int getDialogLayoutResource() {
            return mDialogLayoutResId;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return false;
        }

    }

    public static class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
        TimePicker mTimePicker;
        public static TimePreferenceDialogFragmentCompat newInstance(
                String key) {
            final TimePreferenceDialogFragmentCompat
                    fragment = new TimePreferenceDialogFragmentCompat();
            final Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);

            return fragment;
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);

            mTimePicker = (TimePicker) view.findViewById(R.id.edit);

            // Exception when there is no TimePicker
            if (mTimePicker == null) {
                throw new IllegalStateException("Dialog view must contain" +
                        " a TimePicker with id 'edit'");
            }

            // Get the time from the related Preference
            Integer minutesAfterMidnight = null;
            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                minutesAfterMidnight =
                        ((TimePreference) preference).getTime();
            }

            // Set the time to the TimePicker
            if (minutesAfterMidnight != null) {
                int hours = minutesAfterMidnight / 60;
                int minutes = minutesAfterMidnight % 60;
                boolean is24hour = DateFormat.is24HourFormat(getContext());

                mTimePicker.setIs24HourView(is24hour);
                mTimePicker.setCurrentHour(hours);
                mTimePicker.setCurrentMinute(minutes);
            }
        }
        @Override
        public void onDialogClosed(boolean positiveResult) {
            if (positiveResult) {
                // generate value to save
                int hours = mTimePicker.getCurrentHour();
                int minutes = mTimePicker.getCurrentMinute();
                int minutesAfterMidnight = (hours * 60) + minutes;

                // Get the related Preference and save the value
                DialogPreference preference = getPreference();
                if (preference instanceof TimePreference) {
                    TimePreference timePreference =
                            ((TimePreference) preference);
                    // This allows the client to ignore the user value.
                    if (timePreference.callChangeListener(
                            minutesAfterMidnight)) {
                        // Save the value
                        timePreference.setTime(minutesAfterMidnight);
                    }
                }
            }
        }
    }

//    // based on sample code from https://developer.android.com/guide/topics/ui/controls/pickers
//    public static class TimePickerFragment extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
//        }
//
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            // Do something with the time chosen by the user
//
//        }
//    }

}